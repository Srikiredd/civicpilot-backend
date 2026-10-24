package com.civicpilot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.AiVisualGenerationResponse;
import com.civicpilot.service.AiVisualGenerationService;

@RestController
@RequestMapping("/api/ai-visuals")
public class AiVisualGenerationController {

    private final AiVisualGenerationService aiVisualGenerationService;

    public AiVisualGenerationController(
            AiVisualGenerationService aiVisualGenerationService) {

        this.aiVisualGenerationService =
                aiVisualGenerationService;
    }

    @PostMapping("/journey/{journeyId}")
    public ResponseEntity<AiVisualGenerationResponse>
    generateVisual(
            @PathVariable Long journeyId) {

        AiVisualGenerationResponse response =
                aiVisualGenerationService
                        .generateVisualForJourney(journeyId);

        return ResponseEntity.ok(response);
    }
}