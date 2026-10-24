package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "application_readiness")
public class ApplicationReadiness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = false)
    private CitizenJourney journey;

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = false)
    private JourneyStep journeyStep;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String eligibilityStatus;

    @Column(nullable = false)
    private String documentStatus;

    @Column(columnDefinition = "TEXT")
    private String missingDocuments;

    @Column(columnDefinition = "TEXT")
    private String completedChecks;

    @Column(columnDefinition = "TEXT")
    private String warnings;

    @Column(nullable = false)
    private Integer readinessScore;

    @Column(nullable = false)
    private String readinessStatus;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    private LocalDateTime createdAt;

    public ApplicationReadiness() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public CitizenJourney getJourney() {
        return journey;
    }

    public JourneyStep getJourneyStep() {
        return journeyStep;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public String getMissingDocuments() {
        return missingDocuments;
    }

    public String getCompletedChecks() {
        return completedChecks;
    }

    public String getWarnings() {
        return warnings;
    }

    public Integer getReadinessScore() {
        return readinessScore;
    }

    public String getReadinessStatus() {
        return readinessStatus;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJourney(CitizenJourney journey) {
        this.journey = journey;
    }

    public void setJourneyStep(JourneyStep journeyStep) {
        this.journeyStep = journeyStep;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public void setMissingDocuments(String missingDocuments) {
        this.missingDocuments = missingDocuments;
    }

    public void setCompletedChecks(String completedChecks) {
        this.completedChecks = completedChecks;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public void setReadinessScore(Integer readinessScore) {
        this.readinessScore = readinessScore;
    }

    public void setReadinessStatus(String readinessStatus) {
        this.readinessStatus = readinessStatus;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}