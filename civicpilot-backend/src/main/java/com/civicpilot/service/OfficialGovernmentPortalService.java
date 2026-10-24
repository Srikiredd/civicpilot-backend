package com.civicpilot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.civicpilot.dto.CreateOfficialGovernmentPortalRequest;
import com.civicpilot.dto.OfficialGovernmentPortalResponse;
import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.OfficialGovernmentPortal;
import com.civicpilot.repository.GovernmentServiceRepository;
import com.civicpilot.repository.OfficialGovernmentPortalRepository;

@Service
public class OfficialGovernmentPortalService {

    private final OfficialGovernmentPortalRepository portalRepository;

    private final GovernmentServiceRepository governmentServiceRepository;

    public OfficialGovernmentPortalService(
            OfficialGovernmentPortalRepository portalRepository,
            GovernmentServiceRepository governmentServiceRepository) {

        this.portalRepository = portalRepository;
        this.governmentServiceRepository = governmentServiceRepository;
    }

    // CREATE PORTAL
    public OfficialGovernmentPortalResponse createPortal(
            CreateOfficialGovernmentPortalRequest request) {

        GovernmentService governmentService =
                governmentServiceRepository
                        .findById(request.getGovernmentServiceId())
                        .orElseThrow(() ->
                                new RuntimeException("Government service not found"));

        boolean exists =
                portalRepository
                        .existsByGovernmentServiceAndStateAndOfficialUrl(
                                governmentService,
                                request.getState(),
                                request.getOfficialUrl());

        if (exists) {
            throw new RuntimeException(
                    "Portal with this URL already exists for this service and state");
        }

        OfficialGovernmentPortal portal =
                new OfficialGovernmentPortal();

        portal.setGovernmentService(governmentService);
        portal.setPortalName(request.getPortalName());
        portal.setOfficialUrl(request.getOfficialUrl());
        portal.setState(request.getState());
        portal.setLanguage(request.getLanguage());
        portal.setDescription(request.getDescription());
        portal.setActive(request.isActive());

        OfficialGovernmentPortal saved =
                portalRepository.save(portal);

        return convertToResponse(saved);
    }

    // GET PORTAL BY ID
    public OfficialGovernmentPortalResponse getPortalById(
            Long portalId) {

        OfficialGovernmentPortal portal =
                portalRepository.findById(portalId)
                        .orElseThrow(() ->
                                new RuntimeException("Portal not found"));

        return convertToResponse(portal);
    }

    // GET PORTALS BY SERVICE + STATE + LANGUAGE
    public List<OfficialGovernmentPortalResponse>
    getPortalsByServiceStateAndLanguage(
            Long serviceId,
            String state,
            String language) {

        GovernmentService governmentService =
                governmentServiceRepository.findById(serviceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Government service not found"));

        List<OfficialGovernmentPortal> portals =
                portalRepository
                        .findByGovernmentServiceAndStateAndLanguageAndActiveTrue(
                                governmentService,
                                state,
                                language);

        return portals.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET PORTALS BY SERVICE + STATE
    public List<OfficialGovernmentPortalResponse>
    getPortalsByServiceAndState(
            Long serviceId,
            String state) {

        GovernmentService governmentService =
                governmentServiceRepository.findById(serviceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Government service not found"));

        List<OfficialGovernmentPortal> portals =
                portalRepository
                        .findByGovernmentServiceAndStateAndActiveTrue(
                                governmentService,
                                state);

        return portals.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET PORTALS BY SERVICE + LANGUAGE
    public List<OfficialGovernmentPortalResponse>
    getPortalsByServiceAndLanguage(
            Long serviceId,
            String language) {

        GovernmentService governmentService =
                governmentServiceRepository.findById(serviceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Government service not found"));

        List<OfficialGovernmentPortal> portals =
                portalRepository
                        .findByGovernmentServiceAndLanguageAndActiveTrue(
                                governmentService,
                                language);

        return portals.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET ALL PORTALS FOR SERVICE
    public List<OfficialGovernmentPortalResponse>
    getAllPortalsByService(
            Long serviceId) {

        GovernmentService governmentService =
                governmentServiceRepository.findById(serviceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Government service not found"));

        List<OfficialGovernmentPortal> portals =
                portalRepository
                        .findByGovernmentServiceAndActiveTrue(
                                governmentService);

        return portals.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ENTITY → RESPONSE
    private OfficialGovernmentPortalResponse convertToResponse(
            OfficialGovernmentPortal portal) {

        OfficialGovernmentPortalResponse response =
                new OfficialGovernmentPortalResponse();

        response.setId(portal.getId());

        response.setGovernmentServiceId(
                portal.getGovernmentService().getId());

        response.setPortalName(
                portal.getPortalName());

        response.setOfficialUrl(
                portal.getOfficialUrl());

        response.setState(
                portal.getState());

        response.setLanguage(
                portal.getLanguage());

        response.setDescription(
                portal.getDescription());

        response.setActive(
                portal.isActive());

        response.setCreatedAt(
                portal.getCreatedAt());

        return response;
    }
}