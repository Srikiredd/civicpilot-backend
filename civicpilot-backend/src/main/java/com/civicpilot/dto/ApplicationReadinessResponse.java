package com.civicpilot.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ApplicationReadinessResponse {

    private Long readinessId;

    private Long journeyId;

    private Long stepId;

    private String serviceName;

    private String eligibilityStatus;

    private String documentStatus;

    private List<String> missingDocuments;

    private List<String> completedChecks;

    private List<String> warnings;

    private Integer readinessScore;

    private String readinessStatus;

    private String recommendation;

    private LocalDateTime createdAt;

    public ApplicationReadinessResponse() {
    }

    public ApplicationReadinessResponse(
            Long readinessId,
            Long journeyId,
            Long stepId,
            String serviceName,
            String eligibilityStatus,
            String documentStatus,
            List<String> missingDocuments,
            List<String> completedChecks,
            List<String> warnings,
            Integer readinessScore,
            String readinessStatus,
            String recommendation,
            LocalDateTime createdAt) {

        this.readinessId = readinessId;
        this.journeyId = journeyId;
        this.stepId = stepId;
        this.serviceName = serviceName;
        this.eligibilityStatus = eligibilityStatus;
        this.documentStatus = documentStatus;
        this.missingDocuments = missingDocuments;
        this.completedChecks = completedChecks;
        this.warnings = warnings;
        this.readinessScore = readinessScore;
        this.readinessStatus = readinessStatus;
        this.recommendation = recommendation;
        this.createdAt = createdAt;
    }

    public Long getReadinessId() {
        return readinessId;
    }

    public Long getJourneyId() {
        return journeyId;
    }

    public Long getStepId() {
        return stepId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public List<String> getMissingDocuments() {
        return missingDocuments;
    }

    public List<String> getCompletedChecks() {
        return completedChecks;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public Integer getReadinessScore() {
        return readinessScore;
    }

    public String getReadinessStatus() {
        return readinessStatus;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}