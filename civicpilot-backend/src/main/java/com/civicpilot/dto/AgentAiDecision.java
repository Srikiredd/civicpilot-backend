package com.civicpilot.dto;

public class AgentAiDecision {

    private String action;
    private String reason;
    private String status;
    private boolean humanActionRequired;
    private String humanActionReason;

    public AgentAiDecision() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isHumanActionRequired() {
        return humanActionRequired;
    }

    public void setHumanActionRequired(boolean humanActionRequired) {
        this.humanActionRequired = humanActionRequired;
    }

    public String getHumanActionReason() {
        return humanActionReason;
    }

    public void setHumanActionReason(String humanActionReason) {
        this.humanActionReason = humanActionReason;
    }
}