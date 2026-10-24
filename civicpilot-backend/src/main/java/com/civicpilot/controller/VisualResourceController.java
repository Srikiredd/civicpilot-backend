package com.civicpilot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.AiVisualContent;
import com.civicpilot.dto.CreateVisualResourceRequest;
import com.civicpilot.dto.VisualResourceResponse;
import com.civicpilot.service.VisualResourceService;

@RestController
@RequestMapping("/api/visual-resources")
public class VisualResourceController {

    private final VisualResourceService visualResourceService;

    public VisualResourceController(
            VisualResourceService visualResourceService) {

        this.visualResourceService =
                visualResourceService;
    }

    // ============================================================
    // CREATE VISUAL RESOURCE
    // ============================================================

    @PostMapping
    public ResponseEntity<VisualResourceResponse> createVisualResource(
            @RequestBody CreateVisualResourceRequest request) {

        VisualResourceResponse response =
                visualResourceService
                        .createVisualResource(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET ONE VISUAL
    // ============================================================

    @GetMapping("/{visualId}")
    public ResponseEntity<VisualResourceResponse> getVisualById(
            @PathVariable Long visualId) {

        VisualResourceResponse response =
                visualResourceService
                        .getVisualById(visualId);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // GET VISUALS FOR SERVICE
    // ============================================================

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<VisualResourceResponse>> getVisualsForService(
            @PathVariable Long serviceId,
            @RequestParam(defaultValue = "English")
            String language) {

        List<VisualResourceResponse> response =
                visualResourceService
                        .getVisualsForService(
                                serviceId,
                                language);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // GET STATIC VISUALS FOR SERVICE
    // ============================================================

    @GetMapping("/service/{serviceId}/static")
    public ResponseEntity<List<VisualResourceResponse>> getStaticVisualsForService(
            @PathVariable Long serviceId,
            @RequestParam(defaultValue = "English")
            String language) {

        List<VisualResourceResponse> response =
                visualResourceService
                        .getStaticVisualsForService(
                                serviceId,
                                language);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // GET VISUALS FOR JOURNEY
    // ============================================================

    @GetMapping("/journey/{journeyId}")
    public ResponseEntity<List<VisualResourceResponse>> getVisualsForJourney(
            @PathVariable Long journeyId,
            @RequestParam(defaultValue = "English")
            String language) {

        List<VisualResourceResponse> response =
                visualResourceService
                        .getVisualsForJourney(
                                journeyId,
                                language);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // GET STATIC VISUALS FOR JOURNEY
    // ============================================================

    @GetMapping("/journey/{journeyId}/static")
    public ResponseEntity<List<VisualResourceResponse>> getStaticVisualsForJourney(
            @PathVariable Long journeyId,
            @RequestParam(defaultValue = "English")
            String language) {

        List<VisualResourceResponse> response =
                visualResourceService
                        .getStaticVisualsForJourney(
                                journeyId,
                                language);

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SAVE AI VISUALS FOR JOURNEY
    // ============================================================

    @PostMapping("/journey/{journeyId}/ai")
    public ResponseEntity<List<VisualResourceResponse>> saveAiVisuals(
            @PathVariable Long journeyId,
            @RequestBody List<AiVisualContent> aiVisualContents) {

        List<VisualResourceResponse> response =
                visualResourceService
                        .saveAiVisuals(
                                journeyId,
                                aiVisualContents);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}