package com.civicpilot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.civicpilot.dto.CitizenJourneyResponse;
import com.civicpilot.entity.User;
import com.civicpilot.repository.UserRepository;
import com.civicpilot.service.CitizenJourneyService;

@RestController
@RequestMapping("/api/journeys")
public class CitizenJourneyController {

    private final CitizenJourneyService citizenJourneyService;
    private final UserRepository userRepository;

    public CitizenJourneyController(
            CitizenJourneyService citizenJourneyService,
            UserRepository userRepository
    ) {
        this.citizenJourneyService =
                citizenJourneyService;

        this.userRepository =
                userRepository;
    }

    @PostMapping("/{analysisId}")
    public ResponseEntity<CitizenJourneyResponse> createJourney(

            @PathVariable Long analysisId,

            @RequestParam String state,

            Authentication authentication
    ) {

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        CitizenJourneyResponse response =
                citizenJourneyService
                        .createJourney(
                                analysisId,
                                state,
                                user
                        );

        return ResponseEntity.ok(
                response
        );
    }

    @GetMapping("/{journeyId}")
    public ResponseEntity<CitizenJourneyResponse> getJourney(

            @PathVariable Long journeyId,

            Authentication authentication
    ) {

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        CitizenJourneyResponse response =
                citizenJourneyService
                        .getJourney(
                                journeyId,
                                user
                        );

        return ResponseEntity.ok(
                response
        );
    }

    @GetMapping
    public ResponseEntity<List<CitizenJourneyResponse>> getAllJourneys(

            Authentication authentication
    ) {

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        List<CitizenJourneyResponse> responses =
                citizenJourneyService
                        .getAllJourneys(
                                user
                        );

        return ResponseEntity.ok(
                responses
        );
    }
}