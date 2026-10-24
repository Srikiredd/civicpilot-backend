package com.civicpilot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.AiVisualContent;
import com.civicpilot.service.AiVisualContentService;

@RestController
@RequestMapping("/api/ai-visual-content")
public class AiVisualContentController {

    private final AiVisualContentService aiVisualContentService;

    public AiVisualContentController(
            AiVisualContentService aiVisualContentService) {

        this.aiVisualContentService =
                aiVisualContentService;
    }

    @PostMapping("/journey/{journeyId}")
    public ResponseEntity<List<AiVisualContent>> generateVisualContent(
            @PathVariable Long journeyId) {

        List<AiVisualContent> response =
                aiVisualContentService
                        .generateVisualContent(journeyId);

        return ResponseEntity.ok(response);
    }
}