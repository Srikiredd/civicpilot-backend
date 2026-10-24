package com.civicpilot.dto;

import java.util.List;

public class AgentStartResponse {

    private Long agentRunId;

    private Long stepExecutionId;

    private String status;

    private Integer currentStep;

    private String serviceName;

    private List<RequiredDocumentResponse> requiredDocuments;

    public AgentStartResponse() {
    }

    public AgentStartResponse(
            Long agentRunId,
            Long stepExecutionId,
            String status,
            Integer currentStep,
            String serviceName,
            List<RequiredDocumentResponse> requiredDocuments) {

        this.agentRunId = agentRunId;
        this.stepExecutionId = stepExecutionId;
        this.status = status;
        this.currentStep = currentStep;
        this.serviceName = serviceName;
        this.requiredDocuments = requiredDocuments;
    }

    public Long getAgentRunId() {
        return agentRunId;
    }

    public Long getStepExecutionId() {
        return stepExecutionId;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public String getServiceName() {
        return serviceName;
    }

    public List<RequiredDocumentResponse> getRequiredDocuments() {
        return requiredDocuments;
    }

    public void setAgentRunId(Long agentRunId) {
        this.agentRunId = agentRunId;
    }

    public void setStepExecutionId(Long stepExecutionId) {
        this.stepExecutionId = stepExecutionId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setRequiredDocuments(
            List<RequiredDocumentResponse> requiredDocuments) {

        this.requiredDocuments = requiredDocuments;
    }
}