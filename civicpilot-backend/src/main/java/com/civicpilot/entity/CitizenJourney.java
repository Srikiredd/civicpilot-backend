package com.civicpilot.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "citizen_journeys")
public class CitizenJourney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "analysis_id", nullable = false, unique = true)
    private ProblemAnalysis problemAnalysis;

    @Column(nullable = false)
    private String lifeEvent;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "journey",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("stepNumber ASC")
    private List<JourneyStep> steps = new ArrayList<>();

    public CitizenJourney() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ProblemAnalysis getProblemAnalysis() {
        return problemAnalysis;
    }

    public String getLifeEvent() {
        return lifeEvent;
    }

    public String getLanguage() {
        return language;
    }

    public String getState() {
        return state;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<JourneyStep> getSteps() {
        return steps;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProblemAnalysis(ProblemAnalysis problemAnalysis) {
        this.problemAnalysis = problemAnalysis;
    }

    public void setLifeEvent(String lifeEvent) {
        this.lifeEvent = lifeEvent;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void addStep(JourneyStep step) {
        steps.add(step);
        step.setJourney(this);
    }
}