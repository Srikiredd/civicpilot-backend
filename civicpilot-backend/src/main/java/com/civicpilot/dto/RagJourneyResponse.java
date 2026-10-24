package com.civicpilot.dto;

import java.util.List;

public class RagJourneyResponse {

    private String language;
    private String summary;
    private List<String> recommendedOrder;
    private List<RagJourneyStep> steps;

    public RagJourneyResponse() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getRecommendedOrder() {
        return recommendedOrder;
    }

    public void setRecommendedOrder(List<String> recommendedOrder) {
        this.recommendedOrder = recommendedOrder;
    }

    public List<RagJourneyStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RagJourneyStep> steps) {
        this.steps = steps;
    }
}