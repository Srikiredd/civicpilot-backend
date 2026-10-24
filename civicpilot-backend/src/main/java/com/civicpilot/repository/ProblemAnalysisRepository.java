package com.civicpilot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.ProblemAnalysis;
import com.civicpilot.entity.User;

public interface ProblemAnalysisRepository extends JpaRepository<ProblemAnalysis, Long> {

    List<ProblemAnalysis> findByUser(User user);

    Optional<ProblemAnalysis> findByIdAndUser(Long id, User user);
}