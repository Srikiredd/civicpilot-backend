package com.civicpilot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.Application;
import com.civicpilot.entity.User;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByUser(User user);

    Optional<Application> findByIdAndUser(
            Long id,
            User user);

    Optional<Application> findByApplicationNumber(
            String applicationNumber);

    boolean existsByJourneyStepId(Long journeyStepId);
}