package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "agent_runs")
public class AgentRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "journey_id", nullable = false)
    private CitizenJourney citizenJourney;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgentRunStatus status;

    @Column(nullable = false)
    private Integer currentStepNumber;

    @Column(columnDefinition = "TEXT")
    private String completionUrl;

    @Column(nullable = false)
    private String language;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public AgentRun() {
    }

    public Long getId() {
        return id;
    }

    public CitizenJourney getCitizenJourney() {
        return citizenJourney;
    }

    public AgentRunStatus getStatus() {
        return status;
    }

    public Integer getCurrentStepNumber() {
        return currentStepNumber;
    }

    public String getCompletionUrl() {
        return completionUrl;
    }

    public String getLanguage() {
        return language;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setCitizenJourney(CitizenJourney citizenJourney) {
        this.citizenJourney = citizenJourney;
    }

    public void setStatus(AgentRunStatus status) {
        this.status = status;
    }

    public void setCurrentStepNumber(Integer currentStepNumber) {
        this.currentStepNumber = currentStepNumber;
    }

    public void setCompletionUrl(String completionUrl) {
        this.completionUrl = completionUrl;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();
    }
}