package com.civicpilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.JourneyStep;

public interface JourneyStepRepository
        extends JpaRepository<JourneyStep, Long> {

    List<JourneyStep> findByJourneyOrderByStepNumberAsc(
            CitizenJourney journey
    );
}