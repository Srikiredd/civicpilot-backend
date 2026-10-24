package com.civicpilot.dto;

import java.time.LocalDateTime;

import com.civicpilot.entity.ApplicationStatus;

public class ApplicationTrackingHistoryResponse {

    private ApplicationStatus status;

    private String remarks;

    private LocalDateTime updatedAt;

    public ApplicationTrackingHistoryResponse() {
    }

    public ApplicationTrackingHistoryResponse(
            ApplicationStatus status,
            String remarks,
            LocalDateTime updatedAt) {

        this.status = status;
        this.remarks = remarks;
        this.updatedAt = updatedAt;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}