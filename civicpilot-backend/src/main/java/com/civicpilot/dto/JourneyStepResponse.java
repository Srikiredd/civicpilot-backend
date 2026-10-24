package com.civicpilot.dto;

public class JourneyStepResponse {

    private Long stepId;
    private Integer stepNumber;
    private Long serviceId;
    private String title;
    private String description;
    private String eligibility;
    private String requiredDocuments;
    private String procedure;
    private String status;

    public JourneyStepResponse() {
    }

    public JourneyStepResponse(
            Long stepId,
            Integer stepNumber,
            Long serviceId,
            String title,
            String description,
            String eligibility,
            String requiredDocuments,
            String procedure,
            String status
    ) {
        this.stepId = stepId;
        this.stepNumber = stepNumber;
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.eligibility = eligibility;
        this.requiredDocuments = requiredDocuments;
        this.procedure = procedure;
        this.status = status;
    }

    public Long getStepId() {
        return stepId;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getEligibility() {
        return eligibility;
    }

    public String getRequiredDocuments() {
        return requiredDocuments;
    }

    public String getProcedure() {
        return procedure;
    }

    public String getStatus() {
        return status;
    }
}