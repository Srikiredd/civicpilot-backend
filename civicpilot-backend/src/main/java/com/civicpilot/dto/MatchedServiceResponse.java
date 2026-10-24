package com.civicpilot.dto;

public class MatchedServiceResponse {

    private Long serviceId;
    private String name;
    private String description;
    private String category;
    private String department;
    private String eligibility;
    private String requiredDocuments;
    private String procedureDescription;
    private String matchedReason;

    public MatchedServiceResponse() {
    }

    public MatchedServiceResponse(
            Long serviceId,
            String name,
            String description,
            String category,
            String department,
            String eligibility,
            String requiredDocuments,
            String procedureDescription,
            String matchedReason) {

        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.department = department;
        this.eligibility = eligibility;
        this.requiredDocuments = requiredDocuments;
        this.procedureDescription = procedureDescription;
        this.matchedReason = matchedReason;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDepartment() {
        return department;
    }

    public String getEligibility() {
        return eligibility;
    }

    public String getRequiredDocuments() {
        return requiredDocuments;
    }

    public String getProcedureDescription() {
        return procedureDescription;
    }

    public String getMatchedReason() {
        return matchedReason;
    }
}