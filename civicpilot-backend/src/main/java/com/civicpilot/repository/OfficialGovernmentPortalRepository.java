package com.civicpilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.OfficialGovernmentPortal;

public interface OfficialGovernmentPortalRepository
        extends JpaRepository<OfficialGovernmentPortal, Long> {

    List<OfficialGovernmentPortal>
    findByGovernmentServiceAndStateAndLanguageAndActiveTrue(
            GovernmentService governmentService,
            String state,
            String language);

    List<OfficialGovernmentPortal>
    findByGovernmentServiceAndStateAndActiveTrue(
            GovernmentService governmentService,
            String state);

    List<OfficialGovernmentPortal>
    findByGovernmentServiceAndLanguageAndActiveTrue(
            GovernmentService governmentService,
            String language);

    List<OfficialGovernmentPortal>
    findByGovernmentServiceAndActiveTrue(
            GovernmentService governmentService);

    boolean existsByGovernmentServiceAndStateAndOfficialUrl(
            GovernmentService governmentService,
            String state,
            String officialUrl);
}