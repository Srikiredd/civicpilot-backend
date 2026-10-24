package com.civicpilot.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProblemAnalysisResponse {

    private Long analysisId;
    private String problemDescription;
    private String language;
    private String lifeEvent;
    private List<String> detectedNeeds;
    private LocalDateTime createdAt;

    public ProblemAnalysisResponse() {
    }

    public ProblemAnalysisResponse(
            Long analysisId,
            String problemDescription,
            String language,
            String lifeEvent,
            List<String> detectedNeeds,
            LocalDateTime createdAt) {

        this.analysisId = analysisId;
        this.problemDescription = problemDescription;
        this.language = language;
        this.lifeEvent = lifeEvent;
        this.detectedNeeds = detectedNeeds;
        this.createdAt = createdAt;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getLanguage() {
        return language;
    }

    public String getLifeEvent() {
        return lifeEvent;
    }

    public List<String> getDetectedNeeds() {
        return detectedNeeds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}