package com.civicpilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.VisualResource;
import com.civicpilot.entity.VisualType;

public interface VisualResourceRepository
        extends JpaRepository<VisualResource, Long> {

    List<VisualResource> findByGovernmentService(
            GovernmentService governmentService
    );

    List<VisualResource> findByGovernmentServiceAndLanguage(
            GovernmentService governmentService,
            String language
    );

    List<VisualResource> findByGovernmentServiceAndLanguageAndType(
            GovernmentService governmentService,
            String language,
            VisualType type
    );

    List<VisualResource> findByCitizenJourney(
            CitizenJourney citizenJourney
    );

    List<VisualResource> findByCitizenJourneyAndLanguage(
            CitizenJourney citizenJourney,
            String language
    );

    List<VisualResource> findByCitizenJourneyAndLanguageAndType(
            CitizenJourney citizenJourney,
            String language,
            VisualType type
    );
}