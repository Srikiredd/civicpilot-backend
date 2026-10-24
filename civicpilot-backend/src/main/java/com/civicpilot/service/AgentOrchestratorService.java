package com.civicpilot.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.civicpilot.agent.portal.GovernmentPortalAdapter;
import com.civicpilot.agent.portal.PortalExecutionResult;
import com.civicpilot.dto.AgentAiDecision;
import com.civicpilot.dto.AgentStartResponse;
import com.civicpilot.dto.RequiredDocumentResponse;
import com.civicpilot.entity.AgentDocument;
import com.civicpilot.entity.AgentRun;
import com.civicpilot.entity.AgentRunStatus;
import com.civicpilot.entity.AgentStepExecution;
import com.civicpilot.entity.AgentStepStatus;
import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.JourneyStep;
import com.civicpilot.entity.OfficialGovernmentPortal;
import com.civicpilot.repository.AgentDocumentRepository;
import com.civicpilot.repository.AgentRunRepository;
import com.civicpilot.repository.AgentStepExecutionRepository;
import com.civicpilot.repository.CitizenJourneyRepository;
import com.civicpilot.repository.OfficialGovernmentPortalRepository;

@Service
public class AgentOrchestratorService {

    private final CitizenJourneyRepository journeyRepository;
    private final AgentRunRepository agentRunRepository;
    private final AgentStepExecutionRepository stepExecutionRepository;
    private final AgentDocumentRepository documentRepository;
    private final AgentEmailService emailService;
    private final GovernmentPortalAdapter portalAdapter;
    private final OfficialGovernmentPortalRepository portalRepository;
    private final AgentAiDecisionService aiDecisionService;

    public AgentOrchestratorService(
            CitizenJourneyRepository journeyRepository,
            AgentRunRepository agentRunRepository,
            AgentStepExecutionRepository stepExecutionRepository,
            AgentDocumentRepository documentRepository,
            AgentEmailService emailService,
            GovernmentPortalAdapter portalAdapter,
            OfficialGovernmentPortalRepository portalRepository,
            AgentAiDecisionService aiDecisionService) {

        this.journeyRepository = journeyRepository;
        this.agentRunRepository = agentRunRepository;
        this.stepExecutionRepository = stepExecutionRepository;
        this.documentRepository = documentRepository;
        this.emailService = emailService;
        this.portalAdapter = portalAdapter;
        this.portalRepository = portalRepository;
        this.aiDecisionService = aiDecisionService;
    }

    @Transactional
    public AgentStartResponse startAgent(Long journeyId) {

        CitizenJourney journey =
                journeyRepository.findById(journeyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citizen journey not found"));

        if (journey.getSteps() == null
                || journey.getSteps().isEmpty()) {

            throw new RuntimeException(
                    "Journey has no service steps");
        }

        Optional<AgentRun> existingRun =
                agentRunRepository
                        .findByCitizenJourneyId(journeyId);

        if (existingRun.isPresent()) {
            return buildStartResponse(
                    existingRun.get());
        }

        AgentRun run =
                new AgentRun();

        run.setCitizenJourney(journey);
        run.setCurrentStepNumber(1);

        run.setLanguage(
                normalizeLanguage(
                        journey.getLanguage()));

        run.setStatus(
                AgentRunStatus.WAITING_FOR_DOCUMENTS);

        run =
                agentRunRepository.save(run);

        List<JourneyStep> steps =
                journey.getSteps();

        for (JourneyStep journeyStep : steps) {

            AgentStepExecution execution =
                    new AgentStepExecution();

            execution.setAgentRun(run);

            execution.setJourneyStep(
                    journeyStep);

            execution.setStatus(
                    AgentStepStatus.WAITING_FOR_DOCUMENTS);

            GovernmentService service =
                    journeyStep
                            .getGovernmentService();

            execution.setRequiredDocuments(
                    service.getRequiredDocuments());

            OfficialGovernmentPortal portal =
                    findOfficialPortal(
                            service.getId());

            if (portal != null) {

                execution.setPortalUrl(
                        portal.getOfficialUrl());

            } else {

                execution.setPortalUrl(null);
            }

            stepExecutionRepository.save(
                    execution);
        }

        AgentStepExecution firstExecution =
                stepExecutionRepository
                        .findByAgentRunIdAndJourneyStepStepNumber(
                                run.getId(),
                                1)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "First agent step execution not found"));

        String email =
                getCitizenEmail(journey);

        String citizenName =
                journey.getUser().getName();

        String serviceName =
                firstExecution
                        .getJourneyStep()
                        .getGovernmentService()
                        .getName();

        String documents =
                formatDocuments(
                        parseDocuments(
                                firstExecution
                                        .getRequiredDocuments()));

        emailService.sendDocumentsRequiredEmail(
                email,
                citizenName,
                run.getLanguage(),
                applicationNumber(run),
                serviceName,
                documents
        );

        return buildStartResponse(run);
    }

    public AgentStartResponse getCurrentState(
            Long runId) {

        AgentRun run =
                agentRunRepository.findById(runId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent run not found"));

        return buildStartResponse(run);
    }

    @Transactional
    public void executeCurrentStep(Long runId) {

        AgentRun run =
                agentRunRepository.findById(runId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent run not found"));

        if (run.getStatus()
                == AgentRunStatus.COMPLETED) {

            return;
        }

        AgentStepExecution execution =
                stepExecutionRepository
                        .findByAgentRunIdAndJourneyStepStepNumber(
                                runId,
                                run.getCurrentStepNumber())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent step execution not found"));

        List<AgentDocument> documents =
                documentRepository
                        .findByStepExecutionId(
                                execution.getId());

        List<String> requiredDocuments =
                parseDocuments(
                        execution.getRequiredDocuments());

        Set<String> uploadedDocuments =
                documents.stream()
                        .map(AgentDocument::getDocumentType)
                        .map(this::normalizeDocument)
                        .collect(Collectors.toSet());

        List<String> missingDocuments =
                requiredDocuments.stream()
                        .filter(document ->
                                !uploadedDocuments.contains(
                                        normalizeDocument(
                                                document)))
                        .toList();

        if (!missingDocuments.isEmpty()) {

            run.setStatus(
                    AgentRunStatus.WAITING_FOR_DOCUMENTS);

            execution.setStatus(
                    AgentStepStatus.WAITING_FOR_DOCUMENTS);

            agentRunRepository.save(run);
            stepExecutionRepository.save(execution);

            return;
        }

        run.setStatus(
                AgentRunStatus.RUNNING);

        execution.setStatus(
                AgentStepStatus.RUNNING);

        execution.setStartedAt(
                LocalDateTime.now());

        agentRunRepository.save(run);
        stepExecutionRepository.save(execution);

        AgentAiDecision aiDecision =
                aiDecisionService.decide(
                        execution,
                        documents,
                        run.getLanguage());

        if ("FAIL".equalsIgnoreCase(
                aiDecision.getAction())) {

            execution.setStatus(
                    AgentStepStatus.FAILED);

            execution.setFailureReason(
                    aiDecision.getReason());

            run.setStatus(
                    AgentRunStatus.FAILED);

            agentRunRepository.save(run);
            stepExecutionRepository.save(execution);

            return;
        }

        if ("WAIT_FOR_HUMAN_ACTION"
                .equalsIgnoreCase(
                        aiDecision.getAction())
                || aiDecision.isHumanActionRequired()) {

            run.setStatus(
                    AgentRunStatus.WAITING_FOR_HUMAN_ACTION);

            execution.setStatus(
                    AgentStepStatus.WAITING_FOR_HUMAN_ACTION);

            execution.setFailureReason(
                    aiDecision.getHumanActionReason());

            agentRunRepository.save(run);
            stepExecutionRepository.save(execution);

            return;
        }

        
        PortalExecutionResult result =
                portalAdapter.execute(
                        run,
                        execution,
                        documents);

        execution.setPortalUrl(
                result.getPortalUrl());

        if (result.isHumanActionRequired()) {

            run.setStatus(
                    AgentRunStatus.WAITING_FOR_HUMAN_ACTION);

            execution.setStatus(
                    AgentStepStatus.WAITING_FOR_HUMAN_ACTION);

            execution.setFailureReason(
                    result.getMessage());

            agentRunRepository.save(run);
            stepExecutionRepository.save(execution);

            return;
        }

        if (!result.isCompleted()) {

            execution.setStatus(
                    AgentStepStatus.FAILED);

            execution.setFailureReason(
                    result.getMessage());

            run.setStatus(
                    AgentRunStatus.FAILED);

            agentRunRepository.save(run);
            stepExecutionRepository.save(execution);

            return;
        }

        execution.setStatus(
                AgentStepStatus.COMPLETED);

        execution.setCompletedAt(
                LocalDateTime.now());

        execution.setCompletionUrl(
                result.getCompletionUrl());

        stepExecutionRepository.save(
                execution);

        String email =
                getCitizenEmail(
                        run.getCitizenJourney());

        String citizenName =
                run.getCitizenJourney()
                        .getUser()
                        .getName();

        String serviceName =
                execution
                        .getJourneyStep()
                        .getGovernmentService()
                        .getName();

        String applicationNumber =
                applicationNumber(run);

        emailService.sendReadyToSubmitEmail(
                email,
                citizenName,
                run.getLanguage(),
                applicationNumber,
                serviceName
        );

        emailService.sendSubmittedEmail(
                email,
                citizenName,
                run.getLanguage(),
                applicationNumber,
                serviceName
        );

        emailService.sendUnderReviewEmail(
                email,
                citizenName,
                run.getLanguage(),
                applicationNumber,
                serviceName
        );

        emailService.sendVerificationEmail(
                email,
                citizenName,
                run.getLanguage(),
                applicationNumber,
                serviceName
        );

        emailService.sendApprovedEmail(
                email,
                citizenName,
                run.getLanguage(),
                applicationNumber,
                serviceName
        );

        moveToNextStep(run);
    }

    private void moveToNextStep(
            AgentRun run) {

        List<AgentStepExecution> executions =
                stepExecutionRepository
                        .findByAgentRunIdOrderByJourneyStepStepNumber(
                                run.getId());

        int currentStep =
                run.getCurrentStepNumber();

        Optional<AgentStepExecution> nextStep =
                executions.stream()
                        .filter(execution ->
                                execution
                                        .getJourneyStep()
                                        .getStepNumber()
                                        > currentStep)
                        .findFirst();

        if (nextStep.isEmpty()) {

            AgentStepExecution lastStep =
                    executions.get(
                            executions.size() - 1);

            run.setStatus(
                    AgentRunStatus.COMPLETED);

            run.setCompletionUrl(
                    lastStep.getCompletionUrl());

            agentRunRepository.save(run);

            String email =
                    getCitizenEmail(
                            run.getCitizenJourney());

            String citizenName =
                    run.getCitizenJourney()
                            .getUser()
                            .getName();

            String serviceName =
                    lastStep
                            .getJourneyStep()
                            .getGovernmentService()
                            .getName();

            emailService.sendCompletedEmail(
                    email,
                    citizenName,
                    run.getLanguage(),
                    applicationNumber(run),
                    serviceName,
                    run.getCompletionUrl()
            );

            return;
        }

        int nextStepNumber =
                nextStep.get()
                        .getJourneyStep()
                        .getStepNumber();

        run.setCurrentStepNumber(
                nextStepNumber);

        run.setStatus(
                AgentRunStatus.WAITING_FOR_DOCUMENTS);

        nextStep.get().setStatus(
                AgentStepStatus.WAITING_FOR_DOCUMENTS);

        agentRunRepository.save(run);

        stepExecutionRepository.save(
                nextStep.get());

        String email =
                getCitizenEmail(
                        run.getCitizenJourney());

        String citizenName =
                run.getCitizenJourney()
                        .getUser()
                        .getName();

        String serviceName =
                nextStep.get()
                        .getJourneyStep()
                        .getGovernmentService()
                        .getName();

        String documents =
                formatDocuments(
                        parseDocuments(
                                nextStep.get()
                                        .getRequiredDocuments()));

        emailService.sendDocumentsRequiredEmail(
                email,
                citizenName,
                run.getLanguage(),
                applicationNumber(run),
                serviceName,
                documents
        );
    }

    private AgentStartResponse buildStartResponse(
            AgentRun run) {

        AgentStepExecution execution =
                stepExecutionRepository
                        .findByAgentRunIdAndJourneyStepStepNumber(
                                run.getId(),
                                run.getCurrentStepNumber())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Current agent step not found"));

        List<AgentDocument> documents =
                documentRepository
                        .findByStepExecutionId(
                                execution.getId());

        Set<String> uploaded =
                documents.stream()
                        .map(AgentDocument::getDocumentType)
                        .map(this::normalizeDocument)
                        .collect(Collectors.toSet());

        List<RequiredDocumentResponse>
                requiredDocuments =
                parseDocuments(
                        execution
                                .getRequiredDocuments())
                        .stream()
                        .map(document ->
                                new RequiredDocumentResponse(
                                        document,
                                        uploaded.contains(
                                                normalizeDocument(
                                                        document))))
                        .toList();

        return new AgentStartResponse(
                run.getId(),
                execution.getId(),
                run.getStatus().name(),
                run.getCurrentStepNumber(),
                execution
                        .getJourneyStep()
                        .getGovernmentService()
                        .getName(),
                requiredDocuments);
    }

    private String applicationNumber(
            AgentRun run) {

        return String.format(
                "CP-%013d",
                run.getId());
    }

    private String formatDocuments(
            List<String> documents) {

        if (documents == null
                || documents.isEmpty()) {

            return "No additional documents.";
        }

        StringBuilder result =
                new StringBuilder();

        for (String document : documents) {

            result.append("- ")
                    .append(document)
                    .append("\n");
        }

        return result.toString();
    }

    private List<String> parseDocuments(
            String value) {

        if (value == null
                || value.isBlank()) {

            return Collections.emptyList();
        }

        return Arrays.stream(
                value.split("[,\\n]"))
                .map(String::trim)
                .filter(valueItem ->
                        !valueItem.isBlank())
                .toList();
    }

    private String normalizeDocument(
            String value) {

        return value
                .toLowerCase()
                .replaceAll(
                        "[^a-z0-9]",
                        "");
    }

    private String normalizeLanguage(
            String language) {

        if (language == null
                || language.isBlank()) {

            return "English";
        }

        return language;
    }

    private String getCitizenEmail(
            CitizenJourney journey) {

        return journey
                .getUser()
                .getEmail();
    }

    private OfficialGovernmentPortal
            findOfficialPortal(
                    Long serviceId) {

        List<OfficialGovernmentPortal> portals =
                portalRepository.findAll();

        for (OfficialGovernmentPortal portal :
                portals) {

            if (portal.getGovernmentService()
                    == null) {

                continue;
            }

            if (!portal
                    .getGovernmentService()
                    .getId()
                    .equals(serviceId)) {

                continue;
            }

            if (portal.isActive()) {

                return portal;
            }
        }

        return null;
    }
}