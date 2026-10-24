package com.civicpilot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.civicpilot.dto.CitizenJourneyResponse;
import com.civicpilot.dto.JourneyStepResponse;
import com.civicpilot.dto.RagJourneyResponse;
import com.civicpilot.dto.RagJourneyStep;
import com.civicpilot.dto.VisualResourceResponse;
import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.JourneyStep;
import com.civicpilot.entity.ProblemAnalysis;
import com.civicpilot.entity.User;
import com.civicpilot.entity.VisualResource;
import com.civicpilot.entity.VisualType;
import com.civicpilot.repository.CitizenJourneyRepository;
import com.civicpilot.repository.GovernmentServiceRepository;
import com.civicpilot.repository.JourneyStepRepository;
import com.civicpilot.repository.ProblemAnalysisRepository;
import com.civicpilot.repository.VisualResourceRepository;

@Service
public class CitizenJourneyService {

    private final CitizenJourneyRepository citizenJourneyRepository;

    private final JourneyStepRepository journeyStepRepository;

    private final ProblemAnalysisRepository problemAnalysisRepository;

    private final GovernmentServiceRepository governmentServiceRepository;

    private final CivicPilotRagService civicPilotRagService;

    private final VisualResourceRepository visualResourceRepository;

    public CitizenJourneyService(
            CitizenJourneyRepository citizenJourneyRepository,
            JourneyStepRepository journeyStepRepository,
            ProblemAnalysisRepository problemAnalysisRepository,
            GovernmentServiceRepository governmentServiceRepository,
            CivicPilotRagService civicPilotRagService,
            VisualResourceRepository visualResourceRepository
    ) {

        this.citizenJourneyRepository =
                citizenJourneyRepository;

        this.journeyStepRepository =
                journeyStepRepository;

        this.problemAnalysisRepository =
                problemAnalysisRepository;

        this.governmentServiceRepository =
                governmentServiceRepository;

        this.civicPilotRagService =
                civicPilotRagService;

        this.visualResourceRepository =
                visualResourceRepository;
    }

    // ============================================================
    // CREATE CITIZEN JOURNEY
    // ============================================================

    @Transactional
    public CitizenJourneyResponse createJourney(
            Long analysisId,
            String state,
            User user
    ) {

        // --------------------------------------------------------
        // STEP 1: Find problem analysis
        // --------------------------------------------------------

        ProblemAnalysis analysis =
                problemAnalysisRepository
                        .findByIdAndUser(
                                analysisId,
                                user
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Problem analysis not found"
                                )
                        );

        // --------------------------------------------------------
        // STEP 2: Check whether journey already exists
        // --------------------------------------------------------

        if (citizenJourneyRepository
                .findByProblemAnalysis(analysis)
                .isPresent()) {

            throw new RuntimeException(
                    "A journey already exists for this problem analysis"
            );
        }

        // --------------------------------------------------------
        // STEP 3: Validate state
        // --------------------------------------------------------

        if (state == null ||
                state.isBlank()) {

            throw new RuntimeException(
                    "State is required"
            );
        }

        // --------------------------------------------------------
        // STEP 4: Normalize language
        // --------------------------------------------------------

        String language =
                normalizeLanguage(
                        analysis.getLanguage()
                );

        // --------------------------------------------------------
        // STEP 5: Generate personalized RAG journey
        // --------------------------------------------------------

        com.civicpilot.dto.RagRequest ragRequest =
                new com.civicpilot.dto.RagRequest();

        ragRequest.setAnalysisId(
                analysisId
        );

        ragRequest.setState(
                state.trim()
        );

        RagJourneyResponse ragResponse =
                civicPilotRagService.generateJourney(
                        ragRequest,
                        user
                );

        // --------------------------------------------------------
        // Validate RAG response
        // --------------------------------------------------------

        if (ragResponse == null ||
                ragResponse.getSteps() == null ||
                ragResponse.getSteps().isEmpty()) {

            throw new RuntimeException(
                    "RAG could not generate a personalized journey"
            );
        }

        // --------------------------------------------------------
        // STEP 6: Create CitizenJourney
        // --------------------------------------------------------

        CitizenJourney journey =
                new CitizenJourney();

        journey.setUser(
                user
        );

        journey.setProblemAnalysis(
                analysis
        );

        journey.setLifeEvent(
                analysis.getLifeEvent()
        );

        journey.setLanguage(
                language
        );

        journey.setState(
                state.trim()
        );

        journey.setStatus(
                "NOT_STARTED"
        );

        CitizenJourney savedJourney =
                citizenJourneyRepository.save(
                        journey
                );

        // --------------------------------------------------------
        // STEP 7: Convert RAG steps into JourneySteps
        // --------------------------------------------------------

        int stepNumber = 1;

        for (RagJourneyStep ragStep :
                ragResponse.getSteps()) {

            // Ignore null steps

            if (ragStep == null) {
                continue;
            }

            // Ignore steps without service ID

            if (ragStep.getServiceId() == null) {
                continue;
            }

            // ----------------------------------------------------
            // Find REAL GovernmentService from database
            // ----------------------------------------------------

            GovernmentService governmentService =
                    governmentServiceRepository
                            .findById(
                                    ragStep.getServiceId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Government service not found: "
                                                    + ragStep.getServiceId()
                                    )
                            );

            // ----------------------------------------------------
            // Create JourneyStep
            // ----------------------------------------------------

            JourneyStep step =
                    new JourneyStep();

            step.setJourney(
                    savedJourney
            );

            step.setGovernmentService(
                    governmentService
            );

            step.setStepNumber(
                    stepNumber
            );

            // ----------------------------------------------------
            // Service title
            // ----------------------------------------------------

            if (ragStep.getServiceName() != null &&
                    !ragStep.getServiceName().isBlank()) {

                step.setTitle(
                        ragStep.getServiceName()
                );

            } else {

                step.setTitle(
                        governmentService.getName()
                );
            }

            // ----------------------------------------------------
            // Personalized description
            // ----------------------------------------------------

            if (ragStep.getPurpose() != null &&
                    !ragStep.getPurpose().isBlank()) {

                step.setDescription(
                        ragStep.getPurpose()
                );

            } else {

                step.setDescription(
                        governmentService.getDescription()
                );
            }

            // ----------------------------------------------------
            // Trusted eligibility from database
            // ----------------------------------------------------

            step.setEligibility(
                    governmentService.getEligibility()
            );

            // ----------------------------------------------------
            // Required documents
            // ----------------------------------------------------

            step.setRequiredDocuments(
                    convertListToText(
                            ragStep.getRequiredDocuments()
                    )
            );

            // ----------------------------------------------------
            // Procedure
            // ----------------------------------------------------

            step.setProcedureDescription(
                    convertListToNumberedText(
                            ragStep.getProcedure()
                    )
            );

            // ----------------------------------------------------
            // Initial status
            // ----------------------------------------------------

            step.setStatus(
                    "NOT_STARTED"
            );

            // ----------------------------------------------------
            // Save JourneyStep
            // ----------------------------------------------------

            journeyStepRepository.save(
                    step
            );

            stepNumber++;
        }

        // --------------------------------------------------------
        // STEP 8: Make sure at least one step was created
        // --------------------------------------------------------

        if (stepNumber == 1) {

            throw new RuntimeException(
                    "No valid journey steps could be created"
            );
        }

        // --------------------------------------------------------
        // STEP 9: Return final journey
        // --------------------------------------------------------

        return convertToResponse(
                savedJourney
        );
    }

    // ============================================================
    // CONVERT LIST TO TEXT
    // ============================================================

    private String convertListToText(
            List<String> values
    ) {

        if (values == null ||
                values.isEmpty()) {

            return "";
        }

        return String.join(
                ", ",
                values
        );
    }

    // ============================================================
    // CONVERT PROCEDURE LIST TO NUMBERED TEXT
    // ============================================================

    private String convertListToNumberedText(
            List<String> values
    ) {

        if (values == null ||
                values.isEmpty()) {

            return "";
        }

        StringBuilder result =
                new StringBuilder();

        int number = 1;

        for (String value :
                values) {

            if (value == null ||
                    value.isBlank()) {

                continue;
            }

            result.append(number)
                    .append(". ")
                    .append(value.trim())
                    .append("\n");

            number++;
        }

        return result.toString().trim();
    }

    // ============================================================
    // NORMALIZE LANGUAGE
    // ============================================================

    private String normalizeLanguage(
            String language
    ) {

        if (language == null ||
                language.isBlank()) {

            return "English";
        }

        String normalized =
                language
                        .trim()
                        .toLowerCase();

        switch (normalized) {

            case "english":
                return "English";

            case "telugu":
                return "Telugu";

            case "hindi":
                return "Hindi";

            case "tamil":
                return "Tamil";

            case "kannada":
                return "Kannada";

            case "malayalam":
                return "Malayalam";

            case "marathi":
                return "Marathi";

            case "bengali":
                return "Bengali";

            case "gujarati":
                return "Gujarati";

            case "punjabi":
                return "Punjabi";

            case "urdu":
                return "Urdu";

            case "odia":
            case "oriya":
                return "Odia";

            default:
                throw new RuntimeException(
                        "Unsupported language: "
                                + language
                );
        }
    }

    // ============================================================
    // GET ONE JOURNEY
    // ============================================================

    public CitizenJourneyResponse getJourney(
            Long journeyId,
            User user
    ) {

        CitizenJourney journey =
                citizenJourneyRepository
                        .findByIdAndUser(
                                journeyId,
                                user
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Journey not found"
                                )
                        );

        return convertToResponse(
                journey
        );
    }

    // ============================================================
    // GET ALL JOURNEYS
    // ============================================================

    public List<CitizenJourneyResponse> getAllJourneys(
            User user
    ) {

        List<CitizenJourney> journeys =
                citizenJourneyRepository
                        .findByUser(
                                user
                        );

        List<CitizenJourneyResponse> responses =
                new ArrayList<>();

        for (CitizenJourney journey :
                journeys) {

            responses.add(
                    convertToResponse(
                            journey
                    )
            );
        }

        return responses;
    }

    // ============================================================
    // CONVERT JOURNEY TO RESPONSE
    // ============================================================

    private CitizenJourneyResponse convertToResponse(
            CitizenJourney journey
    ) {

        // --------------------------------------------------------
        // Get journey steps
        // --------------------------------------------------------

        List<JourneyStep> steps =
                journeyStepRepository
                        .findByJourneyOrderByStepNumberAsc(
                                journey
                        );

        List<JourneyStepResponse> stepResponses =
                new ArrayList<>();

        for (JourneyStep step :
                steps) {

            JourneyStepResponse response =
                    new JourneyStepResponse(

                            step.getId(),

                            step.getStepNumber(),

                            step.getGovernmentService()
                                    .getId(),

                            step.getTitle(),

                            step.getDescription(),

                            step.getEligibility(),

                            step.getRequiredDocuments(),

                            step.getProcedureDescription(),

                            step.getStatus()
                    );

            stepResponses.add(
                    response
            );
        }

        // --------------------------------------------------------
        // Get STATIC visual guidance from database
        // --------------------------------------------------------

        List<VisualResourceResponse> visualGuidance =
                new ArrayList<>();

        String language =
                normalizeLanguage(
                        journey.getLanguage()
                );

        // --------------------------------------------------------
        // Get visuals for every journey step
        // --------------------------------------------------------

        for (JourneyStep step :
                steps) {

            GovernmentService service =
                    step.getGovernmentService();

            List<VisualResource> visuals =
                    visualResourceRepository
                            .findByGovernmentServiceAndLanguageAndType(
                                    service,
                                    language,
                                    VisualType.STATIC
                            );

            for (VisualResource visual :
                    visuals) {

                VisualResourceResponse response =
                        new VisualResourceResponse(

                                visual.getId(),

                                service.getId(),

                                service.getName(),

                                visual.getTitle(),

                                visual.getDescription(),

                                visual.getType(),

                                visual.getImageUrl(),

                                visual.getOfficialLink(),

                                visual.getLanguage(),

                                visual.getCreatedAt()
                        );

                visualGuidance.add(
                        response
                );
            }
        }

        // --------------------------------------------------------
        // Return journey + steps + visual guidance
        // --------------------------------------------------------

        return new CitizenJourneyResponse(

                journey.getId(),

                journey.getProblemAnalysis().getId(),

                journey.getLifeEvent(),

                journey.getLanguage(),

                journey.getStatus(),

                journey.getCreatedAt(),

                stepResponses,

                visualGuidance
        );
    }
}