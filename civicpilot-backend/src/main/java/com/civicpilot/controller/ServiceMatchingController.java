package com.civicpilot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.MatchedServiceResponse;
import com.civicpilot.entity.User;
import com.civicpilot.repository.UserRepository;
import com.civicpilot.service.ServiceMatchingService;

@RestController
@RequestMapping("/api/service-matching")
public class ServiceMatchingController {

    private final ServiceMatchingService serviceMatchingService;
    private final UserRepository userRepository;

    public ServiceMatchingController(
            ServiceMatchingService serviceMatchingService,
            UserRepository userRepository) {

        this.serviceMatchingService = serviceMatchingService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{analysisId}")
    public ResponseEntity<List<MatchedServiceResponse>> matchServices(
            @PathVariable Long analysisId,
            Authentication authentication) {

        // Get logged-in user's email from JWT
        String email = authentication.getName();

        // Find user
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Match services
        List<MatchedServiceResponse> response =
                serviceMatchingService.matchServices(
                        analysisId,
                        user);

        return ResponseEntity.ok(response);
    }
}