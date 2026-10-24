package com.civicpilot.dto;

public class CreateOfficialGovernmentPortalRequest {

    private Long governmentServiceId;

    private String portalName;

    private String officialUrl;

    private String state;

    private String language;

    private String description;

    private boolean active;

    public Long getGovernmentServiceId() {
        return governmentServiceId;
    }

    public void setGovernmentServiceId(Long governmentServiceId) {
        this.governmentServiceId = governmentServiceId;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}