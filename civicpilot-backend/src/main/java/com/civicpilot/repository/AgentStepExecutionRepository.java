package com.civicpilot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.AgentStepExecution;

public interface AgentStepExecutionRepository
        extends JpaRepository<AgentStepExecution, Long> {

    List<AgentStepExecution>
    findByAgentRunIdOrderByJourneyStepStepNumber(
            Long agentRunId);

    Optional<AgentStepExecution>
    findByAgentRunIdAndJourneyStepStepNumber(
            Long agentRunId,
            Integer stepNumber);
}