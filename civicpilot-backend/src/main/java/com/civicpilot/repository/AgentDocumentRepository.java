package com.civicpilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.AgentDocument;

public interface AgentDocumentRepository
        extends JpaRepository<AgentDocument, Long> {

    List<AgentDocument>
    findByStepExecutionId(Long stepExecutionId);
}