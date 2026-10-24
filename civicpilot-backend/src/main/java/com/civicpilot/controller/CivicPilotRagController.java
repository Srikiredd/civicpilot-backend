package com.civicpilot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.RagJourneyResponse;
import com.civicpilot.dto.RagRequest;
import com.civicpilot.entity.User;
import com.civicpilot.repository.UserRepository;
import com.civicpilot.service.CivicPilotRagService;

@RestController
@RequestMapping("/api/rag")
public class CivicPilotRagController {

    private final CivicPilotRagService ragService;
    private final UserRepository userRepository;

    public CivicPilotRagController(
            CivicPilotRagService ragService,
            UserRepository userRepository) {

        this.ragService = ragService;
        this.userRepository = userRepository;
    }

    @PostMapping("/journey")
    public ResponseEntity<RagJourneyResponse> generateJourney(
            @RequestBody RagRequest request,
            Authentication authentication) {

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        RagJourneyResponse response =
                ragService.generateJourney(
                        request,
                        user);

        return ResponseEntity.ok(response);
    }
}