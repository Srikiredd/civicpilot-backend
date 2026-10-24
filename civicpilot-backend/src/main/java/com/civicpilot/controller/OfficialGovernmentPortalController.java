package com.civicpilot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.CreateOfficialGovernmentPortalRequest;
import com.civicpilot.dto.OfficialGovernmentPortalResponse;
import com.civicpilot.service.OfficialGovernmentPortalService;

@RestController
@RequestMapping("/api/official-portals")
public class OfficialGovernmentPortalController {

    private final OfficialGovernmentPortalService portalService;

    public OfficialGovernmentPortalController(
            OfficialGovernmentPortalService portalService) {

        this.portalService = portalService;
    }

    // CREATE PORTAL
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfficialGovernmentPortalResponse createPortal(
            @RequestBody CreateOfficialGovernmentPortalRequest request) {

        return portalService.createPortal(request);
    }

    // GET PORTAL BY ID
    @GetMapping("/{portalId}")
    public OfficialGovernmentPortalResponse getPortalById(
            @PathVariable Long portalId) {

        return portalService.getPortalById(portalId);
    }

    // GET BY SERVICE + STATE + LANGUAGE
    @GetMapping("/service/{serviceId}")
    public List<OfficialGovernmentPortalResponse>
    getPortalsByServiceStateAndLanguage(
            @PathVariable Long serviceId,
            @RequestParam String state,
            @RequestParam String language) {

        return portalService.getPortalsByServiceStateAndLanguage(
                serviceId,
                state,
                language);
    }

    // GET BY SERVICE + STATE
    @GetMapping("/service/{serviceId}/state")
    public List<OfficialGovernmentPortalResponse>
    getPortalsByServiceAndState(
            @PathVariable Long serviceId,
            @RequestParam String state) {

        return portalService.getPortalsByServiceAndState(
                serviceId,
                state);
    }

    // GET BY SERVICE + LANGUAGE
    @GetMapping("/service/{serviceId}/language")
    public List<OfficialGovernmentPortalResponse>
    getPortalsByServiceAndLanguage(
            @PathVariable Long serviceId,
            @RequestParam String language) {

        return portalService.getPortalsByServiceAndLanguage(
                serviceId,
                language);
    }

    // GET ALL FOR SERVICE
    @GetMapping("/service/{serviceId}/all")
    public List<OfficialGovernmentPortalResponse>
    getAllPortalsByService(
            @PathVariable Long serviceId) {

        return portalService.getAllPortalsByService(serviceId);
    }
}