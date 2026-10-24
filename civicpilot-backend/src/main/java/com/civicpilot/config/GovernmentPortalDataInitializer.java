package com.civicpilot.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.civicpilot.entity.GovernmentService;
import com.civicpilot.entity.OfficialGovernmentPortal;
import com.civicpilot.repository.GovernmentServiceRepository;
import com.civicpilot.repository.OfficialGovernmentPortalRepository;

@Component
public class GovernmentPortalDataInitializer
        implements CommandLineRunner {

    private final GovernmentServiceRepository governmentServiceRepository;

    private final OfficialGovernmentPortalRepository portalRepository;

    private final IndianStatePortalConfig statePortalConfig;


    public GovernmentPortalDataInitializer(
            GovernmentServiceRepository governmentServiceRepository,
            OfficialGovernmentPortalRepository portalRepository,
            IndianStatePortalConfig statePortalConfig) {

        this.governmentServiceRepository =
                governmentServiceRepository;

        this.portalRepository =
                portalRepository;

        this.statePortalConfig =
                statePortalConfig;
    }


    @Override
    public void run(String... args) {

        System.out.println(
                "==============================================");

        System.out.println(
                "CivicPilot AI - Government Portal Initializer");

        System.out.println(
                "==============================================");


        List<IndianStatePortalConfig.StatePortal> states =
                statePortalConfig.getAllStatePortals();


        for (IndianStatePortalConfig.StatePortal state : states) {

            initializeStateServices(state);
        }


        System.out.println(
                "==============================================");

        System.out.println(
                "Government portal initialization completed.");

        System.out.println(
                "==============================================");
    }


    private void initializeStateServices(
            IndianStatePortalConfig.StatePortal statePortal) {


        // ==========================================
        // STATE-SPECIFIC SERVICES
        // ==========================================

        List<String> stateServices = Arrays.asList(

                "Death Certificate",

                "Birth Certificate",

                "Marriage Certificate",

                "Legal Heir Certificate",

                "Property Mutation",

                "Property Registration",

                "Encumbrance Certificate",

                "Land Ownership Certificate",

                "Income Certificate",

                "Caste Certificate",

                "Residence Certificate",

                "Domicile Certificate",

                "EWS Certificate",

                "Disability Certificate",

                "Senior Citizen Certificate",

                "Widow Certificate",

                "Pension Application",

                "Old Age Pension",

                "Widow Pension",

                "Disability Pension",

                "Ration Card",

                "Ration Card Member Addition",

                "Ration Card Member Removal",

                "Ration Card Address Update",

                "Electricity Connection",

                "Electricity Name Transfer",

                "Water Connection",

                "Government Housing Assistance",

                "Government Health Scheme Registration",

                "Business Registration",

                "Trade Licence",

                "Building Permission",

                "Employment Registration",

                "Unemployment Assistance",

                "Student Certificate",

                "Government Scholarship"
        );


        // ==========================================
        // NATIONAL SERVICES
        // ==========================================

        Set<String> nationalServices = new HashSet<>(Arrays.asList(

                "Aadhaar Address Update",

                "Aadhaar Mobile Number Update",

                "PAN Card Application",

                "PAN Card Correction",

                "Voter ID Registration",

                "Voter ID Address Update",

                "Driving Licence Application",

                "Driving Licence Renewal",

                "Vehicle Registration",

                "Vehicle Ownership Transfer",

                "Passport Application",

                "Passport Renewal"
        ));


        // ==========================================
        // SPECIAL / EXTERNAL SERVICES
        // ==========================================

        Set<String> externalServices = new HashSet<>(Arrays.asList(

                "Bank Asset Transfer",

                "Insurance Claim"
        ));


        // ==========================================
        // PROCESS STATE-SPECIFIC SERVICES
        // ==========================================

        for (String serviceName : stateServices) {

            GovernmentService service =
                    findExistingService(serviceName);

            if (service == null) {
                continue;
            }


            createPortalIfRequired(
                    service,
                    statePortal,
                    statePortal.getPortalName(),
                    statePortal.getOfficialUrl(),
                    "English"
            );
        }


        // ==========================================
        // PROCESS NATIONAL SERVICES
        // ==========================================

        for (String serviceName : nationalServices) {

            GovernmentService service =
                    findExistingService(serviceName);

            if (service == null) {
                continue;
            }


            createNationalPortal(
                    service,
                    statePortal.getState(),
                    serviceName
            );
        }


        // ==========================================
        // EXTERNAL SERVICES
        // ==========================================

        for (String serviceName : externalServices) {

            GovernmentService service =
                    findExistingService(serviceName);

            if (service == null) {
                continue;
            }


            createExternalServicePortal(
                    service,
                    statePortal.getState(),
                    serviceName
            );
        }
    }


    // ==========================================================
    // FIND EXISTING GOVERNMENT SERVICE
    // ==========================================================

    private GovernmentService findExistingService(
            String serviceName) {

        List<GovernmentService> services =
                governmentServiceRepository
                        .findByNameContainingIgnoreCase(serviceName);


        if (services == null || services.isEmpty()) {

            System.out.println(
                    "Service not found in database: "
                            + serviceName);

            return null;
        }


        /*
         * IMPORTANT:
         *
         * Your database currently contains duplicate
         * service names such as:
         *
         * Death Certificate
         * Property Mutation
         * Legal Heir Certificate
         *
         * Therefore we use the first existing service
         * for this initializer.
         *
         * We are NOT creating new GovernmentService rows.
         */

        return services.get(0);
    }


    // ==========================================================
    // CREATE STATE PORTAL
    // ==========================================================

    private void createPortalIfRequired(
            GovernmentService service,
            IndianStatePortalConfig.StatePortal statePortal,
            String portalName,
            String url,
            String language) {


        boolean exists =
                portalRepository
                        .existsByGovernmentServiceAndStateAndOfficialUrl(
                                service,
                                statePortal.getState(),
                                url
                        );


        if (exists) {

            System.out.println(
                    "Already exists: "
                            + service.getName()
                            + " | "
                            + statePortal.getState());

            return;
        }


        OfficialGovernmentPortal portal =
                new OfficialGovernmentPortal();


        portal.setGovernmentService(service);

        portal.setPortalName(portalName);

        portal.setOfficialUrl(url);

        portal.setState(
                statePortal.getState());

        portal.setLanguage(language);

        portal.setDescription(
                "Official government service portal for "
                        + service.getName()
                        + " in "
                        + statePortal.getState()
        );

        portal.setActive(true);


        portalRepository.save(portal);


        System.out.println(
                "Created: "
                        + service.getName()
                        + " | "
                        + statePortal.getState());
    }


    // ==========================================================
    // NATIONAL SERVICES
    // ==========================================================

    private void createNationalPortal(
            GovernmentService service,
            String state,
            String serviceName) {


        String portalName;

        String url;


        switch (serviceName) {

            case "Aadhaar Address Update":
            case "Aadhaar Mobile Number Update":

                portalName = "UIDAI MyAadhaar";

                url =
                        "https://myaadhaar.uidai.gov.in/";

                break;


            case "PAN Card Application":
            case "PAN Card Correction":

                portalName =
                        "Income Tax Department";

                url =
                        "https://www.incometax.gov.in/";

                break;


            case "Voter ID Registration":
            case "Voter ID Address Update":

                portalName =
                        "Election Commission of India";

                url =
                        "https://voters.eci.gov.in/";

                break;


            case "Driving Licence Application":
            case "Driving Licence Renewal":
            case "Vehicle Registration":
            case "Vehicle Ownership Transfer":

                portalName =
                        "Parivahan Sewa";

                url =
                        "https://parivahan.gov.in/";

                break;


            case "Passport Application":
            case "Passport Renewal":

                portalName =
                        "Passport Seva";

                url =
                        "https://www.passportindia.gov.in/";

                break;


            default:

                return;
        }


        createPortalIfRequired(
                service,
                findStatePortal(state),
                portalName,
                url,
                "English"
        );
    }


    // ==========================================================
    // EXTERNAL SERVICES
    // ==========================================================

    private void createExternalServicePortal(
            GovernmentService service,
            String state,
            String serviceName) {


        String portalName;

        String url;


        if (serviceName.equals(
                "Bank Asset Transfer")) {

            portalName =
                    "Concerned Bank / Financial Institution";

            url =
                    "https://www.rbi.org.in/";
        }

        else if (serviceName.equals(
                "Insurance Claim")) {

            portalName =
                    "IRDAI";

            url =
                    "https://irdai.gov.in/";
        }

        else {

            return;
        }


        createPortalIfRequired(
                service,
                findStatePortal(state),
                portalName,
                url,
                "English"
        );
    }


    // ==========================================================
    // FIND STATE CONFIGURATION
    // ==========================================================

    private IndianStatePortalConfig.StatePortal
    findStatePortal(String state) {


        return statePortalConfig
                .getAllStatePortals()
                .stream()
                .filter(
                        portal ->
                                portal.getState()
                                        .equalsIgnoreCase(state)
                )
                .findFirst()
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "State portal not configured: "
                                                + state
                                )
                );
    }
}