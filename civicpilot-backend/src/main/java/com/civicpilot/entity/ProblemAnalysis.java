package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "problem_analysis")
public class ProblemAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Citizen who submitted the problem
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String problemDescription;

    private String language;

    private String lifeEvent;

    @Column(columnDefinition = "TEXT")
    private String detectedNeeds;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public ProblemAnalysis() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getLanguage() {
        return language;
    }

    public String getLifeEvent() {
        return lifeEvent;
    }

    public String getDetectedNeeds() {
        return detectedNeeds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLifeEvent(String lifeEvent) {
        this.lifeEvent = lifeEvent;
    }

    public void setDetectedNeeds(String detectedNeeds) {
        this.detectedNeeds = detectedNeeds;
    }
}