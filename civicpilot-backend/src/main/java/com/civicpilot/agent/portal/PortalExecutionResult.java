package com.civicpilot.agent.portal;

public class PortalExecutionResult {

    private boolean completed;

    private boolean humanActionRequired;

    private String message;

    private String portalUrl;

    private String completionUrl;

    public PortalExecutionResult() {
    }

    public PortalExecutionResult(
            boolean completed,
            boolean humanActionRequired,
            String message,
            String portalUrl,
            String completionUrl) {

        this.completed = completed;
        this.humanActionRequired = humanActionRequired;
        this.message = message;
        this.portalUrl = portalUrl;
        this.completionUrl = completionUrl;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isHumanActionRequired() {
        return humanActionRequired;
    }

    public String getMessage() {
        return message;
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public String getCompletionUrl() {
        return completionUrl;
    }
}