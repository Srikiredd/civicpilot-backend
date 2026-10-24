package com.civicpilot.dto;

import java.util.List;

public class AiProblemAnalysis {

    private String lifeEvent;

    private List<String> detectedNeeds;

    public AiProblemAnalysis() {
    }

    public String getLifeEvent() {
        return lifeEvent;
    }

    public void setLifeEvent(String lifeEvent) {
        this.lifeEvent = lifeEvent;
    }

    public List<String> getDetectedNeeds() {
        return detectedNeeds;
    }

    public void setDetectedNeeds(List<String> detectedNeeds) {
        this.detectedNeeds = detectedNeeds;
    }
}