package com.civicpilot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.GovernmentService;

public interface GovernmentServiceRepository
        extends JpaRepository<GovernmentService, Long> {

    List<GovernmentService> findByNameContainingIgnoreCase(
            String name);

    Optional<GovernmentService> findByName(String name);
}