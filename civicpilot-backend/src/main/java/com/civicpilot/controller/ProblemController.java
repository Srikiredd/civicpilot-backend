package com.civicpilot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.AnalyzeProblemRequest;
import com.civicpilot.dto.ProblemAnalysisResponse;
import com.civicpilot.entity.User;
import com.civicpilot.repository.UserRepository;
import com.civicpilot.service.ProblemAnalysisService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemAnalysisService problemAnalysisService;
    private final UserRepository userRepository;

    public ProblemController(
            ProblemAnalysisService problemAnalysisService,
            UserRepository userRepository) {

        this.problemAnalysisService =
                problemAnalysisService;

        this.userRepository =
                userRepository;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ProblemAnalysisResponse>
            analyzeProblem(
                    @Valid @RequestBody
                    AnalyzeProblemRequest request,
                    Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ProblemAnalysisResponse response =
                problemAnalysisService.analyzeProblem(
                        request,
                        user
                );

        return ResponseEntity.ok(response);
    }
}