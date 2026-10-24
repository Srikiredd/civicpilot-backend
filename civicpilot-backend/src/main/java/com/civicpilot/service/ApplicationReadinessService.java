package com.civicpilot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.civicpilot.dto.ApplicationReadinessRequest;
import com.civicpilot.dto.ApplicationReadinessResponse;
import com.civicpilot.entity.ApplicationReadiness;
import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.JourneyStep;
import com.civicpilot.entity.User;
import com.civicpilot.repository.ApplicationReadinessRepository;
import com.civicpilot.repository.CitizenJourneyRepository;
import com.civicpilot.repository.JourneyStepRepository;

@Service
public class ApplicationReadinessService {

    private final ApplicationReadinessRepository readinessRepository;
    private final JourneyStepRepository journeyStepRepository;
    private final CitizenJourneyRepository citizenJourneyRepository;

    public ApplicationReadinessService(
            ApplicationReadinessRepository readinessRepository,
            JourneyStepRepository journeyStepRepository,
            CitizenJourneyRepository citizenJourneyRepository) {

        this.readinessRepository = readinessRepository;
        this.journeyStepRepository = journeyStepRepository;
        this.citizenJourneyRepository = citizenJourneyRepository;
    }

    public ApplicationReadinessResponse checkReadiness(
            ApplicationReadinessRequest request,
            User user) {

        JourneyStep step = journeyStepRepository
                .findById(request.getStepId())
                .orElseThrow(() ->
                        new RuntimeException("Journey step not found"));

        CitizenJourney journey = citizenJourneyRepository
                .findByIdAndUser(step.getJourney().getId(), user)
                .orElseThrow(() ->
                        new RuntimeException("Journey not found"));

        if (readinessRepository.findByJourneyStep(step).isPresent()) {
            throw new RuntimeException(
                    "Readiness check already exists for this journey step");
        }

        List<String> missingDocuments = new ArrayList<>();
        List<String> completedChecks = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        int score = 0;

        /*
         * 1. Eligibility Check
         */
        if (Boolean.TRUE.equals(request.getEligible())) {

            completedChecks.add("Eligibility check passed");
            score += 25;

        } else {

            warnings.add("Eligibility requirements are not satisfied");
        }

        /*
         * 2. Document Check
         */
        String requiredDocuments = step.getRequiredDocuments();

        String availableDocuments = request.getAvailableDocuments();

        if (requiredDocuments != null
                && !requiredDocuments.isBlank()) {

            List<String> requiredList = Arrays.stream(
                    requiredDocuments.split(","))
                    .map(String::trim)
                    .filter(document -> !document.isBlank())
                    .toList();

            List<String> availableList = new ArrayList<>();

            if (availableDocuments != null
                    && !availableDocuments.isBlank()) {

                availableList = Arrays.stream(
                        availableDocuments.split(","))
                        .map(String::trim)
                        .filter(document -> !document.isBlank())
                        .map(String::toLowerCase)
                        .toList();
            }

            for (String required : requiredList) {

                boolean found = false;

                String requiredLower =
                        required.toLowerCase().trim();

                for (String available : availableList) {

                    if (available.contains(requiredLower)
                            || requiredLower.contains(available)) {

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    missingDocuments.add(required);
                }
            }

            if (missingDocuments.isEmpty()) {

                completedChecks.add(
                        "All required documents are available");

                score += 30;

            } else {

                warnings.add(
                        "Some required documents are missing");
            }
        }

        /*
         * 3. Information Consistency Check
         */
        if (Boolean.TRUE.equals(request.getInformationConsistent())) {

            completedChecks.add(
                    "Information consistency check passed");

            score += 20;

        } else {

            warnings.add(
                    "Information inconsistency may cause application problems");
        }

        /*
         * 4. Procedure Check
         */
        if (Boolean.TRUE.equals(request.getProcedureCompleted())) {

            completedChecks.add(
                    "Required procedure steps completed");

            score += 25;

        } else {

            warnings.add(
                    "Required procedure steps are not completed");
        }

        /*
         * Final status
         */
        String readinessStatus;

        if (score >= 80 && missingDocuments.isEmpty()) {

            readinessStatus = "READY";

        } else {

            readinessStatus = "NEEDS_ATTENTION";
        }

        String documentStatus;

        if (missingDocuments.isEmpty()) {

            documentStatus = "COMPLETE";

        } else {

            documentStatus = "INCOMPLETE";
        }

        String eligibilityStatus;

        if (Boolean.TRUE.equals(request.getEligible())) {

            eligibilityStatus = "ELIGIBLE";

        } else {

            eligibilityStatus = "NOT_ELIGIBLE";
        }

        String recommendation;

        if ("READY".equals(readinessStatus)) {

            recommendation =
                    "Your application appears ready for submission. "
                    + "Verify the official government requirements before submitting.";

        } else {

            recommendation =
                    "Complete the missing requirements and resolve the "
                    + "warnings before submitting the application.";
        }

        /*
         * Save result
         */
        ApplicationReadiness readiness =
                new ApplicationReadiness();

        readiness.setUser(user);
        readiness.setJourney(journey);
        readiness.setJourneyStep(step);
        readiness.setServiceName(
                step.getGovernmentService().getName());

        readiness.setEligibilityStatus(eligibilityStatus);
        readiness.setDocumentStatus(documentStatus);

        readiness.setMissingDocuments(
                String.join(",", missingDocuments));

        readiness.setCompletedChecks(
                String.join(",", completedChecks));

        readiness.setWarnings(
                String.join(",", warnings));

        readiness.setReadinessScore(score);
        readiness.setReadinessStatus(readinessStatus);
        readiness.setRecommendation(recommendation);

        ApplicationReadiness saved =
                readinessRepository.save(readiness);

        return convertToResponse(saved);
    }

    public ApplicationReadinessResponse getReadiness(
            Long readinessId,
            User user) {

        ApplicationReadiness readiness =
                readinessRepository
                        .findByIdAndUser(readinessId, user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Readiness record not found"));

        return convertToResponse(readiness);
    }

    public List<ApplicationReadinessResponse> getAllReadiness(
            User user) {

        List<ApplicationReadiness> records =
                readinessRepository.findByUser(user);

        List<ApplicationReadinessResponse> responses =
                new ArrayList<>();

        for (ApplicationReadiness readiness : records) {

            responses.add(convertToResponse(readiness));
        }

        return responses;
    }

    private ApplicationReadinessResponse convertToResponse(
            ApplicationReadiness readiness) {

        List<String> missingDocuments =
                convertStringToList(
                        readiness.getMissingDocuments());

        List<String> completedChecks =
                convertStringToList(
                        readiness.getCompletedChecks());

        List<String> warnings =
                convertStringToList(
                        readiness.getWarnings());

        return new ApplicationReadinessResponse(
                readiness.getId(),
                readiness.getJourney().getId(),
                readiness.getJourneyStep().getId(),
                readiness.getServiceName(),
                readiness.getEligibilityStatus(),
                readiness.getDocumentStatus(),
                missingDocuments,
                completedChecks,
                warnings,
                readiness.getReadinessScore(),
                readiness.getReadinessStatus(),
                readiness.getRecommendation(),
                readiness.getCreatedAt()
        );
    }

    private List<String> convertStringToList(String value) {

        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }
}