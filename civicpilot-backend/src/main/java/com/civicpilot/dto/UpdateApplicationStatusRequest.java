package com.civicpilot.dto;

import com.civicpilot.entity.ApplicationStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateApplicationStatusRequest {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    private String remarks;

    public UpdateApplicationStatusRequest() {
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}