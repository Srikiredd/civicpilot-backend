package com.civicpilot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.civicpilot.dto.AiVisualContent;
import com.civicpilot.dto.CreateVisualResourceRequest;
import com.civicpilot.dto.VisualResourceResponse;
import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.VisualResource;
import com.civicpilot.entity.VisualType;
import com.civicpilot.repository.CitizenJourneyRepository;
import com.civicpilot.repository.GovernmentServiceRepository;
import com.civicpilot.repository.VisualResourceRepository;

@Service
public class VisualResourceService {

    private final VisualResourceRepository visualResourceRepository;
    private final GovernmentServiceRepository governmentServiceRepository;
    private final CitizenJourneyRepository citizenJourneyRepository;

    public VisualResourceService(
            VisualResourceRepository visualResourceRepository,
            GovernmentServiceRepository governmentServiceRepository,
            CitizenJourneyRepository citizenJourneyRepository) {

        this.visualResourceRepository = visualResourceRepository;
        this.governmentServiceRepository = governmentServiceRepository;
        this.citizenJourneyRepository = citizenJourneyRepository;
    }

    // ============================================================
    // CREATE VISUAL RESOURCE
    // ============================================================

    public VisualResourceResponse createVisualResource(
            CreateVisualResourceRequest request) {

        GovernmentService service = null;
        CitizenJourney journey = null;

        // Find Government Service
        if (request.getServiceId() != null) {

            service = governmentServiceRepository
                    .findById(request.getServiceId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Government service not found"));
        }

        // Find Citizen Journey
        if (request.getJourneyId() != null) {

            journey = citizenJourneyRepository
                    .findById(request.getJourneyId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Citizen journey not found"));
        }

        // At least one must be provided
        if (service == null && journey == null) {

            throw new RuntimeException(
                    "Either serviceId or journeyId is required");
        }

        // Create VisualResource
        VisualResource visual = new VisualResource();

        visual.setGovernmentService(service);
        visual.setCitizenJourney(journey);

        visual.setTitle(request.getTitle());
        visual.setDescription(request.getDescription());
        visual.setType(request.getType());

        visual.setImageUrl(request.getImageUrl());

        visual.setOfficialLink(request.getOfficialLink());

        visual.setLanguage(
                normalizeLanguage(request.getLanguage()));

        VisualResource saved =
                visualResourceRepository.save(visual);

        return convertToResponse(saved);
    }

    // ============================================================
    // SAVE AI VISUALS FOR JOURNEY
    // ============================================================

    public List<VisualResourceResponse> saveAiVisuals(
            Long journeyId,
            List<AiVisualContent> aiVisualContents) {

        // Find journey
        CitizenJourney journey =
                citizenJourneyRepository
                        .findById(journeyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citizen journey not found"));

        // Validate AI response
        if (aiVisualContents == null ||
                aiVisualContents.isEmpty()) {

            throw new RuntimeException(
                    "No AI visual content provided");
        }

        List<VisualResourceResponse> responses =
                new ArrayList<>();

        // Process every AI visual
        for (AiVisualContent aiContent : aiVisualContents) {

            // Find corresponding government service
            GovernmentService service =
                    governmentServiceRepository
                            .findById(
                                    aiContent.getServiceId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Government service not found: "
                                                    + aiContent.getServiceId()));

            // Find local image
            String imageUrl =
                    getImageUrl(
                            aiContent.getServiceId());

            // Create VisualResource
            VisualResource visual =
                    new VisualResource();

            visual.setGovernmentService(service);

            visual.setCitizenJourney(journey);

            visual.setTitle(
                    aiContent.getTitle());

            visual.setDescription(
                    aiContent.getDescription());

            visual.setType(
                    VisualType.AI_GENERATED);

            visual.setImageUrl(
                    imageUrl);

            visual.setImagePrompt(
                    aiContent.getImagePrompt());

            visual.setLanguage(
                    normalizeLanguage(
                            journey.getLanguage()));

            // Save to database
            VisualResource saved =
                    visualResourceRepository.save(
                            visual);

            // Convert to response
            responses.add(
                    convertToResponse(saved));
        }

        return responses;
    }

    // ============================================================
    // AI IMAGE MAPPING
    // ============================================================

    private String getImageUrl(Long serviceId) {

        switch (serviceId.intValue()) {

            // ----------------------------------------------------
            // PROPERTY / LEGAL SERVICES
            // ----------------------------------------------------

            case 2:
            case 5:
            case 11:
                return "/ai-generated-images/property-mutation.png";

            case 3:
            case 7:
                return "/ai-generated-images/death-certificate.png";

            case 4:
            case 10:
                return "/ai-generated-images/legal-heir-certificate.png";

            case 6:
            case 43:
                return "/ai-generated-images/bank-asset-transfer.png";

            case 12:
                return "/ai-generated-images/property-registration.png";

            case 13:
                return "/ai-generated-images/encumbrance-certificate.png";

            case 14:
                return "/ai-generated-images/land-ownership-certificate.png";

            // ----------------------------------------------------
            // CERTIFICATES
            // ----------------------------------------------------

            case 15:
                return "/ai-generated-images/income-certificate.png";

            case 16:
                return "/ai-generated-images/caste-certificate.png";

            case 17:
                return "/ai-generated-images/residence-certificate.png";

            case 18:
                return "/ai-generated-images/domicile-certificate.png";

            case 19:
                return "/ai-generated-images/ews-certificate.png";

            case 20:
                return "/ai-generated-images/disability-certificate.png";

            case 21:
                return "/ai-generated-images/senior-citizen-certificate.png";

            case 22:
                return "/ai-generated-images/widow-certificate.png";

            // ----------------------------------------------------
            // PENSION SERVICES
            // ----------------------------------------------------

            case 23:
                return "/ai-generated-images/pension-application.png";

            case 24:
                throw new RuntimeException(
                        "AI-generated image not available yet for Old Age Pension");

            case 25:
                return "/ai-generated-images/widow-pension.png";

            case 26:
                return "/ai-generated-images/disability-pension.png";

            // ----------------------------------------------------
            // RATION CARD
            // ----------------------------------------------------

            case 27:
                return "/ai-generated-images/ration-card.png";

            case 28:
                return "/ai-generated-images/ration-card-member-addition.png";

            case 29:
                return "/ai-generated-images/ration-card-member-removal.png";

            case 30:
                return "/ai-generated-images/ration-card-address-update.png";

            // ----------------------------------------------------
            // AADHAAR
            // ----------------------------------------------------

            case 31:
                return "/ai-generated-images/aadhaar-address-update.png";

            case 32:
                return "/ai-generated-images/aadhaar-mobile-update.png";

            // ----------------------------------------------------
            // PAN CARD
            // ----------------------------------------------------

            case 33:
                return "/ai-generated-images/pan-card-application.png";

            case 34:
                return "/ai-generated-images/pan-card-correction.png";

            // ----------------------------------------------------
            // VOTER ID
            // ----------------------------------------------------

            case 35:
                return "/ai-generated-images/voter-id-registration.png";

            case 36:
                return "/ai-generated-images/voter-id-address-update.png";

            // ----------------------------------------------------
            // DRIVING LICENCE
            // ----------------------------------------------------

            case 37:
                return "/ai-generated-images/driving-licence-application.png";

            case 38:
                return "/ai-generated-images/driving-licence-renewal.png";

            // ----------------------------------------------------
            // VEHICLE SERVICES
            // ----------------------------------------------------

            case 39:
                return "/ai-generated-images/vehicle-registration.png";

            case 40:
                return "/ai-generated-images/vehicle-ownership-transfer.png";

            // ----------------------------------------------------
            // PASSPORT
            // ----------------------------------------------------

            case 41:
                return "/ai-generated-images/passport-application.png";

            case 42:
                return "/ai-generated-images/passport-renewal.png";

            // ----------------------------------------------------
            // INSURANCE
            // ----------------------------------------------------

            case 44:
                return "/ai-generated-images/insurance-claim.png";

            // ----------------------------------------------------
            // EDUCATION
            // ----------------------------------------------------

            case 45:
                return "/ai-generated-images/government-scholarship.png";

            case 46:
                return "/ai-generated-images/student-certificate.png";

            // ----------------------------------------------------
            // EMPLOYMENT
            // ----------------------------------------------------

            case 47:
                return "/ai-generated-images/employment-registration.png";

            case 48:
                return "/ai-generated-images/unemployment-assistance.png";

            // ----------------------------------------------------
            // BUSINESS
            // ----------------------------------------------------

            case 49:
                return "/ai-generated-images/business-registration.png";

            case 50:
                return "/ai-generated-images/trade-licence.png";

            // ----------------------------------------------------
            // BUILDING / UTILITY SERVICES
            // ----------------------------------------------------

            case 51:
                return "/ai-generated-images/building-permission.png";

            case 52:
                return "/ai-generated-images/water-connection.png";

            case 53:
                return "/ai-generated-images/electricity-connection.png";

            case 54:
                return "/ai-generated-images/electricity-name-transfer.png";

            // ----------------------------------------------------
            // GOVERNMENT ASSISTANCE
            // ----------------------------------------------------

            case 55:
                return "/ai-generated-images/government-housing-assistance.png";

            case 56:
                return "/ai-generated-images/government-health-scheme-registration.png";

            // ----------------------------------------------------
            // NO IMAGE
            // ----------------------------------------------------

            default:
                throw new RuntimeException(
                        "No AI-generated image available for service ID: "
                                + serviceId);
        }
    }

    // ============================================================
    // GET ALL VISUALS FOR SERVICE
    // ============================================================

    public List<VisualResourceResponse> getVisualsForService(
            Long serviceId,
            String language) {

        GovernmentService service =
                governmentServiceRepository
                        .findById(serviceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Government service not found"));

        String normalizedLanguage =
                normalizeLanguage(language);

        List<VisualResource> visuals =
                visualResourceRepository
                        .findByGovernmentServiceAndLanguage(
                                service,
                                normalizedLanguage);

        return convertListToResponse(visuals);
    }

    // ============================================================
    // GET ONE VISUAL
    // ============================================================

    public VisualResourceResponse getVisualById(
            Long visualId) {

        VisualResource visual =
                visualResourceRepository
                        .findById(visualId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Visual resource not found"));

        return convertToResponse(visual);
    }

    // ============================================================
    // GET STATIC VISUALS FOR SERVICE
    // ============================================================

    public List<VisualResourceResponse> getStaticVisualsForService(
            Long serviceId,
            String language) {

        GovernmentService service =
                governmentServiceRepository
                        .findById(serviceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Government service not found"));

        String normalizedLanguage =
                normalizeLanguage(language);

        List<VisualResource> visuals =
                visualResourceRepository
                        .findByGovernmentServiceAndLanguageAndType(
                                service,
                                normalizedLanguage,
                                VisualType.STATIC);

        return convertListToResponse(visuals);
    }

    // ============================================================
    // GET ALL VISUALS FOR JOURNEY
    // ============================================================

    public List<VisualResourceResponse> getVisualsForJourney(
            Long journeyId,
            String language) {

        CitizenJourney journey =
                citizenJourneyRepository
                        .findById(journeyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citizen journey not found"));

        String normalizedLanguage =
                normalizeLanguage(language);

        List<VisualResource> visuals =
                visualResourceRepository
                        .findByCitizenJourneyAndLanguage(
                                journey,
                                normalizedLanguage);

        return convertListToResponse(visuals);
    }

    // ============================================================
    // GET STATIC VISUALS FOR JOURNEY
    // ============================================================

    public List<VisualResourceResponse> getStaticVisualsForJourney(
            Long journeyId,
            String language) {

        CitizenJourney journey =
                citizenJourneyRepository
                        .findById(journeyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Citizen journey not found"));

        String normalizedLanguage =
                normalizeLanguage(language);

        List<VisualResource> visuals =
                visualResourceRepository
                        .findByCitizenJourneyAndLanguageAndType(
                                journey,
                                normalizedLanguage,
                                VisualType.STATIC);

        return convertListToResponse(visuals);
    }

    // ============================================================
    // CONVERT LIST TO RESPONSE
    // ============================================================

    private List<VisualResourceResponse> convertListToResponse(
            List<VisualResource> visuals) {

        List<VisualResourceResponse> responses =
                new ArrayList<>();

        for (VisualResource visual : visuals) {

            responses.add(
                    convertToResponse(visual));
        }

        return responses;
    }

    // ============================================================
    // NORMALIZE LANGUAGE
    // ============================================================

    private String normalizeLanguage(
            String language) {

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
                                + language);
        }
    }

    // ============================================================
    // CONVERT ENTITY TO RESPONSE
    // ============================================================

    private VisualResourceResponse convertToResponse(
            VisualResource visual) {

        Long serviceId = null;
        String serviceName = null;

        if (visual.getGovernmentService() != null) {

            serviceId =
                    visual.getGovernmentService()
                            .getId();

            serviceName =
                    visual.getGovernmentService()
                            .getName();
        }

        return new VisualResourceResponse(

                visual.getId(),

                serviceId,

                serviceName,

                visual.getTitle(),

                visual.getDescription(),

                visual.getType(),

                visual.getImageUrl(),

                visual.getOfficialLink(),

                visual.getLanguage(),

                visual.getCreatedAt()
        );
    }
}