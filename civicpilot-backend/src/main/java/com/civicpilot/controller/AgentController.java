package com.civicpilot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.civicpilot.agent.browser.AgentRuntimeFileStore;
import com.civicpilot.dto.AgentStartResponse;
import com.civicpilot.entity.AgentDocument;
import com.civicpilot.entity.AgentRun;
import com.civicpilot.entity.AgentStepExecution;
import com.civicpilot.repository.AgentDocumentRepository;
import com.civicpilot.repository.AgentRunRepository;
import com.civicpilot.repository.AgentStepExecutionRepository;
import com.civicpilot.service.AgentDocumentService;
import com.civicpilot.service.AgentOrchestratorService;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentOrchestratorService agentOrchestratorService;

    private final AgentRunRepository agentRunRepository;

    private final AgentStepExecutionRepository
            stepExecutionRepository;

    private final AgentDocumentService
            agentDocumentService;

    private final AgentDocumentRepository
            agentDocumentRepository;

    private final AgentRuntimeFileStore
            runtimeFileStore;

    public AgentController(
            AgentOrchestratorService agentOrchestratorService,
            AgentRunRepository agentRunRepository,
            AgentStepExecutionRepository stepExecutionRepository,
            AgentDocumentService agentDocumentService,
            AgentDocumentRepository agentDocumentRepository,
            AgentRuntimeFileStore runtimeFileStore) {

        this.agentOrchestratorService =
                agentOrchestratorService;

        this.agentRunRepository =
                agentRunRepository;

        this.stepExecutionRepository =
                stepExecutionRepository;

        this.agentDocumentService =
                agentDocumentService;

        this.agentDocumentRepository =
                agentDocumentRepository;

        this.runtimeFileStore =
                runtimeFileStore;
    }

    @PostMapping(
            "/journeys/{journeyId}/start")
    public ResponseEntity<AgentStartResponse>
            startAgent(
                    @PathVariable Long journeyId) {

        return ResponseEntity.ok(
                agentOrchestratorService
                        .startAgent(journeyId));
    }

    @GetMapping(
            "/runs/{runId}")
    public ResponseEntity<AgentStartResponse>
            getAgentStatus(
                    @PathVariable Long runId) {

        return ResponseEntity.ok(
                agentOrchestratorService
                        .getCurrentState(runId));
    }

    @PostMapping(
            value =
                "/runs/{runId}/steps/{stepExecutionId}/documents",
            consumes =
                "multipart/form-data")
    public ResponseEntity<AgentStartResponse>
            uploadDocument(
                    @PathVariable Long runId,
                    @PathVariable Long stepExecutionId,
                    @RequestParam String documentType,
                    @RequestParam MultipartFile file) {

        AgentRun run =
                agentRunRepository
                        .findById(runId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent run not found"));

        AgentStepExecution stepExecution =
                stepExecutionRepository
                        .findById(stepExecutionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent step execution not found"));

        if (!stepExecution
                .getAgentRun()
                .getId()
                .equals(run.getId())) {

            throw new RuntimeException(
                    "Agent step does not belong "
                    + "to this agent run");
        }

        AgentDocument document =
                agentDocumentService.saveDocument(
                        run,
                        stepExecution,
                        documentType,
                        file);

        runtimeFileStore.save(
                runId,
                stepExecutionId,
                documentType,
                file);

        agentOrchestratorService
                .executeCurrentStep(runId);

        return ResponseEntity.ok(
                agentOrchestratorService
                        .getCurrentState(runId));
    }

    @PostMapping(
            "/runs/{runId}/execute")
    public ResponseEntity<AgentStartResponse>
            executeAgent(
                    @PathVariable Long runId) {

        agentOrchestratorService
                .executeCurrentStep(runId);

        return ResponseEntity.ok(
                agentOrchestratorService
                        .getCurrentState(runId));
    }
}