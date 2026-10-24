package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "journey_steps")
public class JourneyStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = false)
    private CitizenJourney journey;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private GovernmentService governmentService;

    @Column(nullable = false)
    private Integer stepNumber;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String eligibility;

    @Column(name = "required_documents", columnDefinition = "TEXT")
    private String requiredDocuments;

    @Column(name = "procedure_description", columnDefinition = "TEXT")
    private String procedureDescription;

    @Column(nullable = false)
    private String status;

    private LocalDateTime completedAt;

    public JourneyStep() {
    }

    public Long getId() {
        return id;
    }

    public CitizenJourney getJourney() {
        return journey;
    }

    public GovernmentService getGovernmentService() {
        return governmentService;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getEligibility() {
        return eligibility;
    }

    public String getRequiredDocuments() {
        return requiredDocuments;
    }

    public String getProcedureDescription() {
        return procedureDescription;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setJourney(CitizenJourney journey) {
        this.journey = journey;
    }

    public void setGovernmentService(GovernmentService governmentService) {
        this.governmentService = governmentService;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public void setRequiredDocuments(String requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
    }

    public void setProcedureDescription(String procedureDescription) {
        this.procedureDescription = procedureDescription;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}