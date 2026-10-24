package com.civicpilot.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.service.GovernmentServiceVectorSearchService;

@RestController
@RequestMapping("/api/vector-search")
public class VectorSearchController {

    private final GovernmentServiceVectorSearchService searchService;

    public VectorSearchController(
            GovernmentServiceVectorSearchService searchService) {

        this.searchService = searchService;
    }

    @GetMapping
    public List<Document> search(
            @RequestParam String query) {

        return searchService.searchServices(query);
    }
}