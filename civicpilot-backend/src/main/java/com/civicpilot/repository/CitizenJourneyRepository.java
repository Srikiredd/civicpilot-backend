package com.civicpilot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.ProblemAnalysis;
import com.civicpilot.entity.User;

public interface CitizenJourneyRepository
        extends JpaRepository<CitizenJourney, Long> {

    Optional<CitizenJourney> findByIdAndUser(
            Long id,
            User user
    );

    Optional<CitizenJourney> findByProblemAnalysis(
            ProblemAnalysis problemAnalysis
    );

    List<CitizenJourney> findByUser(User user);
}