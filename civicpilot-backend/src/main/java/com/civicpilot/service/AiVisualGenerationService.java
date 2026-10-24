package com.civicpilot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

import com.civicpilot.dto.AiVisualContent;
import com.civicpilot.dto.AiVisualGenerationResponse;
import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.JourneyStep;
import com.civicpilot.entity.VisualResource;
import com.civicpilot.entity.VisualType;
import com.civicpilot.repository.CitizenJourneyRepository;
import com.civicpilot.repository.VisualResourceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiVisualGenerationService {

    private final CitizenJourneyRepository citizenJourneyRepository;
    private final VisualResourceRepository visualResourceRepository;
    private final ChatClient chatClient;
    private final ImageModel imageModel;
    private final ObjectMapper objectMapper;

    private final Path imageDirectory =
            Paths.get("ai-generated-images");

    public AiVisualGenerationService(
            CitizenJourneyRepository citizenJourneyRepository,
            VisualResourceRepository visualResourceRepository,
            ChatClient.Builder chatClientBuilder,
            ImageModel imageModel,
            ObjectMapper objectMapper) {

        this.citizenJourneyRepository = citizenJourneyRepository;
        this.visualResourceRepository = visualResourceRepository;
        this.chatClient = chatClientBuilder.build();
        this.imageModel = imageModel;
        this.objectMapper = objectMapper;
    }

    public AiVisualGenerationResponse generateVisualForJourney(Long journeyId) {

        CitizenJourney journey = citizenJourneyRepository.findById(journeyId)
                .orElseThrow(() ->
                        new RuntimeException("Citizen journey not found"));

        if (journey.getSteps() == null || journey.getSteps().isEmpty()) {
            throw new RuntimeException(
                    "No journey steps available for AI visual generation");
        }

        /*
         * STEP 1
         * Build context from the existing CivicPilot journey.
         */

        String journeyContext = buildJourneyContext(journey);

        /*
         * STEP 2
         * Ask Gemini to create the visual content definition.
         */

        String aiResponse = chatClient.prompt()
                .user("""
                        You are the visual guidance designer for CivicPilot AI,
                        an e-governance platform.

                        Create visual guidance for the citizen journey below.

                        IMPORTANT:
                        - Use only the government-service information provided.
                        - Do not invent government requirements.
                        - Make the explanation simple for an ordinary citizen.
                        - The image should be an educational infographic.
                        - Show the journey as clear visual steps.
                        - Use icons, arrows, documents, departments and
                          application stages where appropriate.
                        - Do not create fake government logos.
                        - Do not create fake URLs.
                        - Do not claim that an application was submitted.

                        Return ONLY valid JSON:

                        {
                          "title": "...",
                          "description": "...",
                          "imagePrompt": "..."
                        }

                        Citizen Journey:
                        %s
                        """.formatted(journeyContext))
                .call()
                .content();

        AiVisualContent aiContent;

        try {
            aiContent = objectMapper.readValue(
                    aiResponse,
                    AiVisualContent.class
            );
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse AI visual content", e);
        }

        /*
         * STEP 3
         * Generate the actual image using Gemini.
         */

        ImagePrompt imagePrompt =
                new ImagePrompt(aiContent.getImagePrompt());

        ImageResponse imageResponse =
                imageModel.call(imagePrompt);

        if (imageResponse == null ||
                imageResponse.getResult() == null ||
                imageResponse.getResult().getOutput() == null) {

            throw new RuntimeException(
                    "Gemini did not return an image");
        }

        Image image =
                imageResponse.getResult().getOutput();

        /*
         * Gemini may return URL or Base64.
         */

        String imageUrl;

        if (image.getUrl() != null &&
                !image.getUrl().isBlank()) {

            imageUrl = image.getUrl();

        } else if (image.getB64Json() != null &&
                !image.getB64Json().isBlank()) {

            imageUrl = saveBase64Image(
                    image.getB64Json());

        } else {

            throw new RuntimeException(
                    "Gemini returned neither image URL nor image data");
        }

        /*
         * STEP 4
         * Save the AI-generated visual metadata in MySQL.
         */

        JourneyStep firstStep = journey.getSteps().get(0);

        GovernmentService service =
                firstStep.getGovernmentService();

        VisualResource visual =
                new VisualResource();

        visual.setGovernmentService(service);

        /*
         * If your current VisualResource contains
         * citizenJourney, keep this line.
         */
        visual.setCitizenJourney(journey);

        visual.setTitle(aiContent.getTitle());

        visual.setDescription(
                aiContent.getDescription());

        visual.setType(
                VisualType.AI_GENERATED);

        visual.setImageUrl(imageUrl);

        visual.setOfficialLink(null);

        visual.setLanguage(
                journey.getLanguage());

        VisualResource saved =
                visualResourceRepository.save(visual);

        return new AiVisualGenerationResponse(
                saved.getId(),
                journey.getId(),
                service.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getImageUrl(),
                saved.getType().name()
        );
    }

    private String buildJourneyContext(
            CitizenJourney journey) {

        StringBuilder context =
                new StringBuilder();

        context.append("Life Event: ")
                .append(journey.getLifeEvent())
                .append("\n");

        context.append("Language: ")
                .append(journey.getLanguage())
                .append("\n");

        context.append("State: ")
                .append(journey.getState())
                .append("\n\n");

        context.append("Journey Steps:\n");

        List<JourneyStep> steps =
                journey.getSteps();

        for (JourneyStep step : steps) {

            GovernmentService service =
                    step.getGovernmentService();

            context.append("\n");

            context.append("Step ")
                    .append(step.getStepNumber())
                    .append("\n");

            context.append("Service: ")
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

    private String saveBase64Image(
            String base64Image) {

        try {

            Files.createDirectories(
                    imageDirectory);

            byte[] imageBytes =
                    Base64.getDecoder()
                            .decode(base64Image);

            String fileName =
                    UUID.randomUUID()
                            + ".png";

            Path filePath =
                    imageDirectory.resolve(fileName);

            Files.write(
                    filePath,
                    imageBytes);

            return "/ai-generated-images/"
                    + fileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to save AI generated image",
                    e);
        }
    }
}