package com.civicpilot.dto;

public class RagRequest {

    private Long analysisId;

    private String state;

    public RagRequest() {
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}