package com.civicpilot.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.civicpilot.entity.ApplicationStatus;

public class ApplicationResponse {

    private Long applicationId;

    private Long journeyId;

    private Long stepId;

    private String applicationNumber;

    private String serviceName;

    private ApplicationStatus status;

    private String remarks;

    private LocalDateTime submittedAt;

    private LocalDateTime lastUpdatedAt;

    private LocalDateTime createdAt;

    private List<ApplicationTrackingHistoryResponse>
            trackingHistory;

    public ApplicationResponse() {
    }

    public ApplicationResponse(
            Long applicationId,
            Long journeyId,
            Long stepId,
            String applicationNumber,
            String serviceName,
            ApplicationStatus status,
            String remarks,
            LocalDateTime submittedAt,
            LocalDateTime lastUpdatedAt,
            LocalDateTime createdAt,
            List<ApplicationTrackingHistoryResponse>
                    trackingHistory) {

        this.applicationId = applicationId;
        this.journeyId = journeyId;
        this.stepId = stepId;
        this.applicationNumber = applicationNumber;
        this.serviceName = serviceName;
        this.status = status;
        this.remarks = remarks;
        this.submittedAt = submittedAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.createdAt = createdAt;
        this.trackingHistory = trackingHistory;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public Long getJourneyId() {
        return journeyId;
    }

    public Long getStepId() {
        return stepId;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<ApplicationTrackingHistoryResponse>
    getTrackingHistory() {
        return trackingHistory;
    }
}