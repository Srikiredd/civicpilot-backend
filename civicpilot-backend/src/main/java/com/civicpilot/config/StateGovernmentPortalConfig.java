package com.civicpilot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StateGovernmentPortalConfig {

    public List<StatePortal> getAllStatePortals() {

        List<StatePortal> portals = new ArrayList<>();

        // =========================
        // STATES
        // =========================

        portals.add(new StatePortal(
                "Andhra Pradesh",
                "AP Seva",
                "https://vswsonline.ap.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Arunachal Pradesh",
                "Arunachal e-Service",
                "https://eservice.arunachal.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Assam",
                "Sewa Setu",
                "https://sewasetu.assam.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Bihar",
                "RTPS Bihar",
                "https://serviceonline.bihar.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Chhattisgarh",
                "e-District",
                "https://edistrict.cgstate.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Goa",
                "Goa Online",
                "https://goaonline.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Gujarat",
                "Digital Gujarat",
                "https://digitalgujarat.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Haryana",
                "Saral Haryana",
                "https://saralharyana.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Himachal Pradesh",
                "e-District",
                "https://edistrict.hp.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Jharkhand",
                "Jharsewa",
                "https://jharsewa.jharkhand.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Karnataka",
                "Seva Sindhu",
                "https://sevasindhu.karnataka.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Kerala",
                "e-Sevanam",
                "https://services.kerala.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Madhya Pradesh",
                "MP e-Service",
                "https://services.mp.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Maharashtra",
                "Aaple Sarkar",
                "https://aaplesarkar.mahaonline.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Manipur",
                "Manipur USP",
                "https://uspmanipur.mn.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Meghalaya",
                "Meghalaya Online",
                "https://meghalayaone.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Mizoram",
                "No Single Unified Portal",
                null,
                false
        ));

        portals.add(new StatePortal(
                "Nagaland",
                "e-District",
                "https://edistrict.nagaland.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Odisha",
                "Odisha One",
                "https://odishaone.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Punjab",
                "Connect Punjab",
                "https://connect.punjab.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Rajasthan",
                "e-Mitra",
                "https://emitra.rajasthan.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Sikkim",
                "Sikkim SSO",
                "https://sso.sikkim.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Tamil Nadu",
                "e-Sevai",
                "https://tnesevai.tn.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Telangana",
                "MeeSeva",
                "https://ts.meeseva.telangana.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Tripura",
                "e-District",
                "https://edistrict.tripura.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Uttar Pradesh",
                "e-District / Nivesh Mitra",
                "https://edistrict.up.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Uttarakhand",
                "Apuni Sarkar",
                "https://eservices.uk.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "West Bengal",
                "e-District",
                "https://edistrict.wb.gov.in/",
                true
        ));

        // =========================
        // UNION TERRITORIES
        // =========================

        portals.add(new StatePortal(
                "Andaman and Nicobar Islands",
                "e-Seva",
                "https://anieseva.andaman.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Chandigarh",
                "e-District",
                "https://eservices.chd.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Dadra and Nagar Haveli and Daman and Diu",
                "Single Window Portal",
                "https://swp.dddgov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Delhi",
                "e-District Delhi",
                "https://edistrict.delhi.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Jammu and Kashmir",
                "e-UNNAT",
                "https://eunnat.jk.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Ladakh",
                "e-Seva",
                "https://eseva.ladakh.gov.in/",
                true
        ));

        portals.add(new StatePortal(
                "Lakshadweep",
                "No Single Unified Portal",
                null,
                false
        ));

        portals.add(new StatePortal(
                "Puducherry",
                "e-District",
                "https://edistrict.py.gov.in/",
                true
        ));

        return portals;
    }


    // ============================================
    // INNER CLASS
    // ============================================

    public static class StatePortal {

        private String state;

        private String portalName;

        private String officialUrl;

        private boolean unifiedPortalAvailable;


        public StatePortal(
                String state,
                String portalName,
                String officialUrl,
                boolean unifiedPortalAvailable) {

            this.state = state;
            this.portalName = portalName;
            this.officialUrl = officialUrl;
            this.unifiedPortalAvailable = unifiedPortalAvailable;
        }


        public String getState() {
            return state;
        }


        public String getPortalName() {
            return portalName;
        }


        public String getOfficialUrl() {
            return officialUrl;
        }


        public boolean isUnifiedPortalAvailable() {
            return unifiedPortalAvailable;
        }
    }
}