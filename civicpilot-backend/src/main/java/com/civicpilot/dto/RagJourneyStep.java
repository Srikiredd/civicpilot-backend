package com.civicpilot.dto;

import java.util.List;

public class RagJourneyStep {

    private Long serviceId;

    private String serviceName;

    private String purpose;

    private List<String> requiredDocuments;

    private List<String> procedure;

    private String visualPrompt;

    private String visualType;

    private String officialPortalName;

    private String officialLink;

    public RagJourneyStep() {
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public List<String> getRequiredDocuments() {
        return requiredDocuments;
    }

    public void setRequiredDocuments(List<String> requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
    }

    public List<String> getProcedure() {
        return procedure;
    }

    public void setProcedure(List<String> procedure) {
        this.procedure = procedure;
    }

    public String getVisualPrompt() {
        return visualPrompt;
    }

    public void setVisualPrompt(String visualPrompt) {
        this.visualPrompt = visualPrompt;
    }

    public String getVisualType() {
        return visualType;
    }

    public void setVisualType(String visualType) {
        this.visualType = visualType;
    }

    public String getOfficialPortalName() {
        return officialPortalName;
    }

    public void setOfficialPortalName(String officialPortalName) {
        this.officialPortalName = officialPortalName;
    }

    public String getOfficialLink() {
        return officialLink;
    }

    public void setOfficialLink(String officialLink) {
        this.officialLink = officialLink;
    }
}