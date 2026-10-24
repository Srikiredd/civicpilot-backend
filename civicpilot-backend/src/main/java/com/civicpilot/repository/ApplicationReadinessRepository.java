package com.civicpilot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.ApplicationReadiness;
import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.JourneyStep;
import com.civicpilot.entity.User;

public interface ApplicationReadinessRepository
        extends JpaRepository<ApplicationReadiness, Long> {

    List<ApplicationReadiness> findByUser(User user);

    List<ApplicationReadiness> findByJourney(CitizenJourney journey);

    Optional<ApplicationReadiness> findByJourneyStep(JourneyStep journeyStep);

    Optional<ApplicationReadiness> findByIdAndUser(
            Long id,
            User user
    );
}