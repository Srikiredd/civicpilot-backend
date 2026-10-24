package com.civicpilot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.AgentRun;

public interface AgentRunRepository
        extends JpaRepository<AgentRun, Long> {

    Optional<AgentRun> findByCitizenJourneyId(Long journeyId);
}