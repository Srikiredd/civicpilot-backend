package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "agent_step_executions")
public class AgentStepExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_run_id", nullable = false)
    private AgentRun agentRun;

    @ManyToOne(optional = false)
    @JoinColumn(name = "journey_step_id", nullable = false)
    private JourneyStep journeyStep;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgentStepStatus status;

    @Column(columnDefinition = "TEXT")
    private String requiredDocuments;

    @Column(columnDefinition = "TEXT")
    private String portalUrl;

    @Column(columnDefinition = "TEXT")
    private String completionUrl;

    @Column(columnDefinition = "TEXT")
    private String failureReason;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    public AgentStepExecution() {
    }

    public Long getId() {
        return id;
    }

    public AgentRun getAgentRun() {
        return agentRun;
    }

    public JourneyStep getJourneyStep() {
        return journeyStep;
    }

    public AgentStepStatus getStatus() {
        return status;
    }

    public String getRequiredDocuments() {
        return requiredDocuments;
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public String getCompletionUrl() {
        return completionUrl;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setAgentRun(AgentRun agentRun) {
        this.agentRun = agentRun;
    }

    public void setJourneyStep(JourneyStep journeyStep) {
        this.journeyStep = journeyStep;
    }

    public void setStatus(AgentStepStatus status) {
        this.status = status;
    }

    public void setRequiredDocuments(String requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    public void setCompletionUrl(String completionUrl) {
        this.completionUrl = completionUrl;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}