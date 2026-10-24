package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "application_tracking_history")
public class ApplicationTrackingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public ApplicationTrackingHistory() {
    }

    public Long getId() {
        return id;
    }

    public Application getApplication() {
        return application;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    public void prePersist() {

        updatedAt = LocalDateTime.now();
    }
}