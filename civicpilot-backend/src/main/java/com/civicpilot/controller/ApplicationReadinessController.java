package com.civicpilot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.ApplicationReadinessRequest;
import com.civicpilot.dto.ApplicationReadinessResponse;
import com.civicpilot.entity.User;
import com.civicpilot.repository.UserRepository;
import com.civicpilot.service.ApplicationReadinessService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/readiness")
public class ApplicationReadinessController {

    private final ApplicationReadinessService readinessService;
    private final UserRepository userRepository;

    public ApplicationReadinessController(
            ApplicationReadinessService readinessService,
            UserRepository userRepository) {

        this.readinessService = readinessService;
        this.userRepository = userRepository;
    }

    @PostMapping("/check")
    public ResponseEntity<ApplicationReadinessResponse> checkReadiness(
            @Valid @RequestBody ApplicationReadinessRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ApplicationReadinessResponse response =
                readinessService.checkReadiness(
                        request,
                        user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{readinessId}")
    public ResponseEntity<ApplicationReadinessResponse> getReadiness(
            @PathVariable Long readinessId,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ApplicationReadinessResponse response =
                readinessService.getReadiness(
                        readinessId,
                        user);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ApplicationReadinessResponse>> getAllReadiness(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<ApplicationReadinessResponse> responses =
                readinessService.getAllReadiness(user);

        return ResponseEntity.ok(responses);
    }
}