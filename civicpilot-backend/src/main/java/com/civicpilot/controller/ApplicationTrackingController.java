package com.civicpilot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.ApplicationResponse;
import com.civicpilot.dto.CreateApplicationRequest;
import com.civicpilot.dto.UpdateApplicationStatusRequest;
import com.civicpilot.entity.User;
import com.civicpilot.repository.UserRepository;
import com.civicpilot.service.ApplicationTrackingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/applications")
public class ApplicationTrackingController {

    private final ApplicationTrackingService
            applicationService;

    private final UserRepository userRepository;

    public ApplicationTrackingController(
            ApplicationTrackingService applicationService,
            UserRepository userRepository) {

        this.applicationService =
                applicationService;

        this.userRepository =
                userRepository;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse>
    createApplication(
            @Valid
            @RequestBody
            CreateApplicationRequest request,
            Authentication authentication) {

        User user =
                getAuthenticatedUser(authentication);

        ApplicationResponse response =
                applicationService.createApplication(
                        request,
                        user);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse>
    updateStatus(
            @PathVariable Long applicationId,

            @Valid
            @RequestBody
            UpdateApplicationStatusRequest request,

            Authentication authentication) {

        User user =
                getAuthenticatedUser(authentication);

        ApplicationResponse response =
                applicationService.updateStatus(
                        applicationId,
                        request,
                        user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse>
    getApplication(
            @PathVariable Long applicationId,
            Authentication authentication) {

        User user =
                getAuthenticatedUser(authentication);

        return ResponseEntity.ok(
                applicationService.getApplication(
                        applicationId,
                        user));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>>
    getAllApplications(
            Authentication authentication) {

        User user =
                getAuthenticatedUser(authentication);

        return ResponseEntity.ok(
                applicationService
                        .getAllApplications(user));
    }

    private User getAuthenticatedUser(
            Authentication authentication) {

        String email =
                authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));
    }
}