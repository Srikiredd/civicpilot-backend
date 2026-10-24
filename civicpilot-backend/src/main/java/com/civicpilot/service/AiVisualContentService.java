package com.civicpilot.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.civicpilot.dto.AiVisualContent;
import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.JourneyStep;
import com.civicpilot.repository.CitizenJourneyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiVisualContentService {

    private final CitizenJourneyRepository citizenJourneyRepository;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AiVisualContentService(
            CitizenJourneyRepository citizenJourneyRepository,
            ChatClient.Builder chatClientBuilder,
            ObjectMapper objectMapper) {

        this.citizenJourneyRepository = citizenJourneyRepository;
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public List<AiVisualContent> generateVisualContent(Long journeyId) {

        // 1. Find the citizen journey
        CitizenJourney journey = citizenJourneyRepository.findById(journeyId)
                .orElseThrow(() ->
                        new RuntimeException("Citizen journey not found"));

        // 2. Make sure journey has steps
        if (journey.getSteps() == null || journey.getSteps().isEmpty()) {
            throw new RuntimeException(
                    "No journey steps available for AI visual generation");
        }

        // 3. Build journey information
        String journeyContext = buildJourneyContext(journey);

        // 4. Send ONE request to Gemini
        String aiResponse = chatClient.prompt()
                .user("""
                        You are the AI visual guidance designer for CivicPilot AI,
                        an e-governance platform.

                        Your task is to create SEPARATE visual guidance content
                        for EVERY journey step provided below.

                        IMPORTANT RULES:

                        1. Generate exactly ONE visual content object for EACH
                           journey step.

                        2. Do NOT combine multiple services into one visual.

                        3. Use ONLY the government-service information provided.

                        4. Do NOT invent eligibility requirements.

                        5. Do NOT invent documents.

                        6. Do NOT invent government procedures.

                        7. Do NOT create fake government portal URLs.

                        8. Do NOT create fake government logos.

                        9. Make the visual content easy for an ordinary citizen
                           to understand.

                        10. The imagePrompt must describe an educational
                            infographic specifically for THAT service.

                        11. The imagePrompt should contain:
                            - service name
                            - department concept
                            - important process steps
                            - relevant document icons
                            - simple directional flow
                            - clear citizen-friendly visual structure

                        12. Do not claim that an application has already
                            been submitted.

                        13. Generate the content in the citizen's selected
                            language.

                        14. Preserve the exact step number and service ID
                            provided in the input.

                        15. Return ONLY valid JSON.

                        Required JSON format:

                        [
                          {
                            "stepNumber": 1,
                            "serviceId": 3,
                            "serviceName": "Death Certificate",
                            "title": "Death Certificate Guidance",
                            "description": "Simple explanation for the citizen",
                            "imagePrompt": "Detailed educational infographic prompt"
                          }
                        ]

                        Return one object for every journey step.

                        Citizen Journey Information:

                        %s
                        """.formatted(journeyContext))
                .call()
                .content();

        // 5. Convert Gemini JSON into a list of DTOs
        try {

            String cleanedResponse = cleanJsonResponse(aiResponse);

            return objectMapper.readValue(
                    cleanedResponse,
                    new TypeReference<List<AiVisualContent>>() {
                    }
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse AI visual content",
                    e
            );
        }
    }

    private String buildJourneyContext(CitizenJourney journey) {

        StringBuilder context = new StringBuilder();

        context.append("Life Event: ")
                .append(journey.getLifeEvent())
                .append("\n");

        context.append("State: ")
                .append(journey.getState())
                .append("\n");

        context.append("Language: ")
                .append(journey.getLanguage())
                .append("\n\n");

        context.append("Journey Steps:\n");

        List<JourneyStep> steps = journey.getSteps();

        for (JourneyStep step : steps) {

            GovernmentService service =
                    step.getGovernmentService();

            context.append("\n");

            context.append("Step Number: ")
                    .append(step.getStepNumber())
                    .append("\n");

            context.append("Service ID: ")
                    .append(service.getId())
                    .append("\n");

            context.append("Service Name: ")
                    .append(service.getName())
                    .append("\n");

            context.append("Description: ")
                    .append(service.getDescription())
                    .append("\n");

            context.append("Department: ")
                    .append(service.getDepartment())
                    .append("\n");

            context.append("Eligibility: ")
                    .append(service.getEligibility())
                    .append("\n");

            context.append("Required Documents: ")
                    .append(service.getRequiredDocuments())
                    .append("\n");

            context.append("Procedure: ")
                    .append(service.getProcedureDescription())
                    .append("\n");
        }

        return context.toString();
    }

    private String cleanJsonResponse(String response) {

        if (response == null || response.isBlank()) {
            throw new RuntimeException(
                    "Gemini returned an empty response");
        }

        String cleaned = response.trim();

        // Remove ```json
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7).trim();
        }

        // Remove ```
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3).trim();
        }

        // Remove ending ```
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(
                    0,
                    cleaned.length() - 3
            ).trim();
        }

        return cleaned;
    }
}