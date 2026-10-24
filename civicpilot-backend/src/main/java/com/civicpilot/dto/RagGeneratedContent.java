package com.civicpilot.dto;

import java.util.List;

public class RagGeneratedContent {

    private String summary;

    private List<String> recommendedOrder;

    private List<RagGeneratedStep> steps;

    public RagGeneratedContent() {
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

    public List<RagGeneratedStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RagGeneratedStep> steps) {
        this.steps = steps;
    }
}