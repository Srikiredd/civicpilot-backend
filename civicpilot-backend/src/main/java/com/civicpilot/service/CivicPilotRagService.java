package com.civicpilot.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.civicpilot.dto.RagGeneratedContent;
import com.civicpilot.dto.RagGeneratedStep;
import com.civicpilot.dto.RagJourneyResponse;
import com.civicpilot.dto.RagJourneyStep;
import com.civicpilot.dto.RagRequest;
import com.civicpilot.entity.OfficialGovernmentPortal;
import com.civicpilot.entity.ProblemAnalysis;
import com.civicpilot.entity.User;
import com.civicpilot.repository.OfficialGovernmentPortalRepository;
import com.civicpilot.repository.ProblemAnalysisRepository;

@Service
public class CivicPilotRagService {

    private final ProblemAnalysisRepository problemAnalysisRepository;

    private final OfficialGovernmentPortalRepository portalRepository;

    private final VectorStore vectorStore;

    private final ChatClient chatClient;

    public CivicPilotRagService(
            ProblemAnalysisRepository problemAnalysisRepository,
            OfficialGovernmentPortalRepository portalRepository,
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder) {

        this.problemAnalysisRepository =
                problemAnalysisRepository;

        this.portalRepository =
                portalRepository;

        this.vectorStore =
                vectorStore;

        this.chatClient =
                chatClientBuilder.build();
    }

    public RagJourneyResponse generateJourney(
            RagRequest request,
            User user) {

        /*
         * STEP 1
         * Find the citizen's previous problem analysis.
         */

        ProblemAnalysis analysis =
                problemAnalysisRepository
                        .findByIdAndUser(
                                request.getAnalysisId(),
                                user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Problem analysis not found"));

        String language =
                analysis.getLanguage();

        String problem =
                analysis.getProblemDescription();

        String detectedNeeds =
                analysis.getDetectedNeeds();

        if (detectedNeeds == null ||
                detectedNeeds.isBlank()) {

            throw new RuntimeException(
                    "No detected government-service needs found");
        }

        /*
         * STEP 2
         * Perform semantic retrieval.
         *
         * The LinkedHashMap stores:
         *
         * REAL DATABASE SERVICE ID
         *          +
         * RETRIEVED DOCUMENT
         *
         * Gemini will NOT decide the service ID.
         */

        Map<Long, Document> retrievedDocuments =
                new LinkedHashMap<>();

        String[] needs =
                detectedNeeds.split(",");

        for (String need : needs) {

            String query =
                    need.trim();

            if (query.isBlank()) {
                continue;
            }

            List<Document> documents =
                    vectorStore.similaritySearch(query);

            if (documents == null) {
                continue;
            }

            /*
             * Take the strongest few results
             * for every detected need.
             */

            int limit =
                    Math.min(3, documents.size());

            for (int i = 0; i < limit; i++) {

                Document document =
                        documents.get(i);

                Object serviceIdObject =
                        document
                                .getMetadata()
                                .get("serviceId");

                if (serviceIdObject == null) {
                    continue;
                }

                Long serviceId;

                if (serviceIdObject instanceof Number) {

                    serviceId =
                            ((Number) serviceIdObject)
                                    .longValue();

                } else {

                    try {

                        serviceId =
                                Long.parseLong(
                                        serviceIdObject.toString());

                    } catch (NumberFormatException e) {

                        continue;
                    }
                }

                /*
                 * Store the REAL database ID.
                 */

                retrievedDocuments.putIfAbsent(
                        serviceId,
                        document);
            }
        }

        if (retrievedDocuments.isEmpty()) {

            throw new RuntimeException(
                    "No relevant government services found");
        }

        /*
         * STEP 3
         * Build RAG context.
         */

        StringBuilder context =
                new StringBuilder();

        for (Map.Entry<Long, Document> entry :
                retrievedDocuments.entrySet()) {

            Long realServiceId =
                    entry.getKey();

            Document document =
                    entry.getValue();

            context.append(
                    "\n-------------------------\n");

            context.append(
                    "DATABASE SERVICE ID: ")
                    .append(realServiceId)
                    .append("\n");

            context.append(
                    document.getText());

            context.append(
                    "\n-------------------------\n");
        }

        /*
         * STEP 4
         * Ask Gemini to generate personalized
         * content.
         *
         * IMPORTANT:
         * Gemini must NOT create service IDs.
         */

        String prompt = """

                You are CivicPilot AI, an intelligent
                government-service journey assistant.

                Citizen problem:
                %s

                Citizen selected language:
                %s

                Detected government-service needs:
                %s

                Retrieved government-service knowledge:
                %s

                TASK:

                Create a personalized step-by-step government
                service journey for this citizen.

                IMPORTANT RULES:

                1. Generate the complete response in the
                   citizen's selected language.

                2. Use ONLY the retrieved government-service
                   information as the factual source.

                3. Do not invent government services.

                4. Do not invent eligibility requirements.

                5. Do not invent required documents.

                6. Do not invent government portal URLs.

                7. Do not create URLs.

                8. Arrange the services in a logical order
                   based on the citizen's situation.

                9. Explain why each service is relevant.

                10. Provide a clear list of required documents.

                11. Provide simple numbered procedures.

                12. Create a visualPrompt for each step.

                    The visualPrompt should describe an
                    informative government-service illustration
                    that can later be sent to an AI image
                    generation system.

                13. Keep the information practical and
                    understandable for an ordinary citizen.

                14. Do not claim that an application will
                    definitely be approved.

                15. DO NOT generate or guess service IDs.

                16. The service ID is controlled by the
                    application and database, not by the AI.

                Return structured data containing:

                - summary
                - recommendedOrder
                - steps

                Each step must contain:

                - serviceId
                - serviceName
                - purpose
                - requiredDocuments
                - procedure
                - visualPrompt

                For serviceId, return 0 because the application
                will replace it with the real database ID.

                """.formatted(
                        problem,
                        language,
                        detectedNeeds,
                        context
                );

        RagGeneratedContent generatedContent =
                chatClient
                        .prompt()
                        .user(prompt)
                        .call()
                        .entity(
                                RagGeneratedContent.class,
                                spec -> spec.validateSchema()
                        );

        /*
         * STEP 5
         *
         * Build final response.
         *
         * IMPORTANT:
         * We map the AI-generated service name
         * back to the REAL database service ID.
         */

        RagJourneyResponse response =
                new RagJourneyResponse();

        response.setLanguage(language);

        response.setSummary(
                generatedContent.getSummary());

        response.setRecommendedOrder(
                generatedContent.getRecommendedOrder());

        List<RagJourneyStep> finalSteps =
                new ArrayList<>();

        for (RagGeneratedStep generatedStep :
                generatedContent.getSteps()) {

            /*
             * Find the real database ID using
             * the service name returned by Gemini.
             */

            Long realServiceId =
                    findServiceIdByName(
                            generatedStep.getServiceName(),
                            retrievedDocuments);

            if (realServiceId == null) {

                /*
                 * Skip an AI-generated service that
                 * does not correspond to a retrieved
                 * database service.
                 */

                continue;
            }

            RagJourneyStep finalStep =
                    new RagJourneyStep();

            /*
             * USE REAL DATABASE ID
             *
             * NOT generatedStep.getServiceId()
             */

            finalStep.setServiceId(
                    realServiceId);

            finalStep.setServiceName(
                    generatedStep.getServiceName());

            finalStep.setPurpose(
                    generatedStep.getPurpose());

            finalStep.setRequiredDocuments(
                    generatedStep.getRequiredDocuments());

            finalStep.setProcedure(
                    generatedStep.getProcedure());

            finalStep.setVisualPrompt(
                    generatedStep.getVisualPrompt());

            /*
             * Phase 7 will use AI-generated visuals.
             */

            finalStep.setVisualType(
                    "AI_GENERATED");

            /*
             * IMPORTANT:
             * Official URL comes from our database,
             * NOT from Gemini.
             */

            OfficialGovernmentPortal portal =
                    findOfficialPortal(
                            realServiceId,
                            request.getState(),
                            language);

            if (portal != null) {

                finalStep.setOfficialPortalName(
                        portal.getPortalName());

                finalStep.setOfficialLink(
                        portal.getOfficialUrl());
            }

            finalSteps.add(finalStep);
        }

        response.setSteps(finalSteps);

        return response;
    }

    /*
     * Find the REAL database service ID by matching
     * the service name generated by Gemini against
     * the services that were actually retrieved.
     */

    private Long findServiceIdByName(
            String serviceName,
            Map<Long, Document> retrievedDocuments) {

        if (serviceName == null ||
                serviceName.isBlank()) {

            return null;
        }

        String aiServiceName =
                serviceName.trim();

        for (Map.Entry<Long, Document> entry :
                retrievedDocuments.entrySet()) {

            Long serviceId =
                    entry.getKey();

            Document document =
                    entry.getValue();

            String documentText =
                    document.getText();

            if (documentText == null) {
                continue;
            }

            /*
             * Extract the service name from the
             * retrieved document.
             */

            String retrievedServiceName =
                    extractServiceName(
                            documentText);

            if (retrievedServiceName != null &&
                    retrievedServiceName.equalsIgnoreCase(
                            aiServiceName)) {

                return serviceId;
            }
        }

        return null;
    }

    /*
     * Extract:
     *
     * Government Service: Death Certificate
     *
     * from the retrieved document.
     */

    private String extractServiceName(
            String documentText) {

        String prefix =
                "Government Service:";

        int start =
                documentText.indexOf(prefix);

        if (start == -1) {
            return null;
        }

        start += prefix.length();

        int end =
                documentText.indexOf(
                        "\n",
                        start);

        if (end == -1) {

            end =
                    documentText.length();
        }

        return documentText
                .substring(start, end)
                .trim();
    }

    /*
     * Find official government portal
     * from our database.
     */

    private OfficialGovernmentPortal findOfficialPortal(
            Long serviceId,
            String state,
            String language) {

        List<OfficialGovernmentPortal> portals =
                portalRepository.findAll();

        for (OfficialGovernmentPortal portal :
                portals) {

            if (portal.getGovernmentService() == null) {
                continue;
            }

            if (!portal.getGovernmentService()
                    .getId()
                    .equals(serviceId)) {

                continue;
            }

            boolean stateMatches =
                    state == null ||
                    state.isBlank() ||
                    state.equalsIgnoreCase(
                            portal.getState());

            boolean languageMatches =
                    language == null ||
                    language.isBlank() ||
                    language.equalsIgnoreCase(
                            portal.getLanguage());

            if (stateMatches &&
                    languageMatches &&
                    portal.isActive()) {

                return portal;
            }
        }

        return null;
    }
}