package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "agent_documents")
public class AgentDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_run_id", nullable = false)
    private AgentRun agentRun;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_step_execution_id", nullable = false)
    private AgentStepExecution stepExecution;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    private LocalDateTime uploadedAt;

    public AgentDocument() {
    }

    public Long getId() {
        return id;
    }

    public AgentRun getAgentRun() {
        return agentRun;
    }

    public AgentStepExecution getStepExecution() {
        return stepExecution;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setAgentRun(AgentRun agentRun) {
        this.agentRun = agentRun;
    }

    public void setStepExecution(AgentStepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @PrePersist
    public void prePersist() {
        uploadedAt = LocalDateTime.now();
    }
}