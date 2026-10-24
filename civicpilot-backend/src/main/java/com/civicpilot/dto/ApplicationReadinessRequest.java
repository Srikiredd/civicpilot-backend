package com.civicpilot.dto;

import jakarta.validation.constraints.NotNull;

public class ApplicationReadinessRequest {

    @NotNull(message = "Journey step ID is required")
    private Long stepId;

    private Boolean eligible;

    private String availableDocuments;

    private Boolean informationConsistent;

    private Boolean procedureCompleted;

    public ApplicationReadinessRequest() {
    }

    public Long getStepId() {
        return stepId;
    }

    public Boolean getEligible() {
        return eligible;
    }

    public String getAvailableDocuments() {
        return availableDocuments;
    }

    public Boolean getInformationConsistent() {
        return informationConsistent;
    }

    public Boolean getProcedureCompleted() {
        return procedureCompleted;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }

    public void setAvailableDocuments(String availableDocuments) {
        this.availableDocuments = availableDocuments;
    }

    public void setInformationConsistent(Boolean informationConsistent) {
        this.informationConsistent = informationConsistent;
    }

    public void setProcedureCompleted(Boolean procedureCompleted) {
        this.procedureCompleted = procedureCompleted;
    }
}