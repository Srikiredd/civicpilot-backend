package com.civicpilot.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CitizenJourneyResponse {

    private Long journeyId;

    private Long analysisId;

    private String lifeEvent;

    private String language;

    private String status;

    private LocalDateTime createdAt;

    private List<JourneyStepResponse> steps;

    private List<VisualResourceResponse> visualGuidance;

    public CitizenJourneyResponse() {
    }

    public CitizenJourneyResponse(
            Long journeyId,
            Long analysisId,
            String lifeEvent,
            String language,
            String status,
            LocalDateTime createdAt,
            List<JourneyStepResponse> steps,
            List<VisualResourceResponse> visualGuidance
    ) {

        this.journeyId = journeyId;

        this.analysisId = analysisId;

        this.lifeEvent = lifeEvent;

        this.language = language;

        this.status = status;

        this.createdAt = createdAt;

        this.steps = steps;

        this.visualGuidance = visualGuidance;
    }

    public Long getJourneyId() {
        return journeyId;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public String getLifeEvent() {
        return lifeEvent;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<JourneyStepResponse> getSteps() {
        return steps;
    }

    public List<VisualResourceResponse>
            getVisualGuidance() {

        return visualGuidance;
    }
}