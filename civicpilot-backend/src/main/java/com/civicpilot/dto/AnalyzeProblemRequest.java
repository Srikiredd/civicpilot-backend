package com.civicpilot.dto;

import jakarta.validation.constraints.NotBlank;

public class AnalyzeProblemRequest {

    @NotBlank(message = "Problem description is required")
    private String problemDescription;

    @NotBlank(message = "Language is required")
    private String language;

    public AnalyzeProblemRequest() {
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getLanguage() {
        return language;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}