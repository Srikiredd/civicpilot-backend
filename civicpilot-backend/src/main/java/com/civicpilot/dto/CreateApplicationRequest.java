package com.civicpilot.dto;

import jakarta.validation.constraints.NotNull;

public class CreateApplicationRequest {

    @NotNull(message = "Journey step ID is required")
    private Long stepId;

    public CreateApplicationRequest() {
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }
}