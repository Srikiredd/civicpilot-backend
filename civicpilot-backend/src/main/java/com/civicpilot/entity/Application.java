package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "applications")
public class Application {

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

    @Column(nullable = false, unique = true)
    private String applicationNumber;

    @Column(nullable = false)
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private LocalDateTime submittedAt;

    private LocalDateTime lastUpdatedAt;

    private LocalDateTime createdAt;

    public Application() {
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

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
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

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();

        lastUpdatedAt = LocalDateTime.now();

        if (status == null) {
            status = ApplicationStatus.DRAFT;
        }
    }

    @PreUpdate
    public void preUpdate() {

        lastUpdatedAt = LocalDateTime.now();
    }
}