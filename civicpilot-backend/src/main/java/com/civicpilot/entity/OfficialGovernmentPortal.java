package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "official_government_portals")
public class OfficialGovernmentPortal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "government_service_id", nullable = false)
    private GovernmentService governmentService;

    @Column(nullable = false)
    private String portalName;

    @Column(nullable = false, length = 500)
    private String officialUrl;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String language;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public OfficialGovernmentPortal() {
    }

    public Long getId() {
        return id;
    }

    public GovernmentService getGovernmentService() {
        return governmentService;
    }

    public void setGovernmentService(GovernmentService governmentService) {
        this.governmentService = governmentService;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}