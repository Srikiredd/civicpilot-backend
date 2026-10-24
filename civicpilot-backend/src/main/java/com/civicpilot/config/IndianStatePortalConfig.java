package com.civicpilot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

@Configuration
public class IndianStatePortalConfig {

    public List<StatePortal> getAllStatePortals() {

        List<StatePortal> portals = new ArrayList<>();

        // ============================
        // STATES
        // ============================

        portals.add(new StatePortal(
                "Andhra Pradesh",
                "AP Seva",
                "https://vswsonline.ap.gov.in/"
        ));

        portals.add(new StatePortal(
                "Arunachal Pradesh",
                "Arunachal e-Service",
                "https://eservice.arunachal.gov.in/"
        ));

        portals.add(new StatePortal(
                "Assam",
                "Sewa Setu",
                "https://sewasetu.assam.gov.in/"
        ));

        portals.add(new StatePortal(
                "Bihar",
                "RTPS Bihar",
                "https://serviceonline.bihar.gov.in/"
        ));

        portals.add(new StatePortal(
                "Chhattisgarh",
                "Chhattisgarh e-District",
                "https://edistrict.cgstate.gov.in/"
        ));

        portals.add(new StatePortal(
                "Goa",
                "Goa Online",
                "https://goaonline.gov.in/"
        ));

        portals.add(new StatePortal(
                "Gujarat",
                "Digital Gujarat",
                "https://digitalgujarat.gov.in/"
        ));

        portals.add(new StatePortal(
                "Haryana",
                "Saral Haryana",
                "https://saralharyana.gov.in/"
        ));

        portals.add(new StatePortal(
                "Himachal Pradesh",
                "Himachal Pradesh e-District",
                "https://edistrict.hp.gov.in/"
        ));

        portals.add(new StatePortal(
                "Jharkhand",
                "Jharsewa",
                "https://jharsewa.jharkhand.gov.in/"
        ));

        portals.add(new StatePortal(
                "Karnataka",
                "Seva Sindhu",
                "https://sevasindhu.karnataka.gov.in/"
        ));

        portals.add(new StatePortal(
                "Kerala",
                "e-Sevanam",
                "https://services.kerala.gov.in/"
        ));

        portals.add(new StatePortal(
                "Madhya Pradesh",
                "MP e-Service",
                "https://services.mp.gov.in/"
        ));

        portals.add(new StatePortal(
                "Maharashtra",
                "Aaple Sarkar",
                "https://aaplesarkar.mahaonline.gov.in/"
        ));

        portals.add(new StatePortal(
                "Manipur",
                "Manipur USP",
                "https://uspmanipur.mn.gov.in/"
        ));

        portals.add(new StatePortal(
                "Meghalaya",
                "Meghalaya Online",
                "https://meghalayaone.gov.in/"
        ));

        portals.add(new StatePortal(
                "Mizoram",
                "Mizoram Government Services",
                "https://mizoram.gov.in/"
        ));

        portals.add(new StatePortal(
                "Nagaland",
                "Nagaland e-District",
                "https://edistrict.nagaland.gov.in/"
        ));

        portals.add(new StatePortal(
                "Odisha",
                "Odisha One",
                "https://odishaone.gov.in/"
        ));

        portals.add(new StatePortal(
                "Punjab",
                "Connect Punjab",
                "https://connect.punjab.gov.in/"
        ));

        portals.add(new StatePortal(
                "Rajasthan",
                "e-Mitra",
                "https://emitra.rajasthan.gov.in/"
        ));

        portals.add(new StatePortal(
                "Sikkim",
                "Sikkim SSO",
                "https://sso.sikkim.gov.in/"
        ));

        portals.add(new StatePortal(
                "Tamil Nadu",
                "e-Sevai",
                "https://tnesevai.tn.gov.in/"
        ));

        portals.add(new StatePortal(
                "Telangana",
                "MeeSeva",
                "https://ts.meeseva.telangana.gov.in/"
        ));

        portals.add(new StatePortal(
                "Tripura",
                "Tripura e-District",
                "https://edistrict.tripura.gov.in/"
        ));

        portals.add(new StatePortal(
                "Uttar Pradesh",
                "UP e-District",
                "https://edistrict.up.gov.in/"
        ));

        portals.add(new StatePortal(
                "Uttarakhand",
                "Apuni Sarkar",
                "https://eservices.uk.gov.in/"
        ));

        portals.add(new StatePortal(
                "West Bengal",
                "West Bengal e-District",
                "https://edistrict.wb.gov.in/"
        ));

        // ============================
        // UNION TERRITORIES
        // ============================

        portals.add(new StatePortal(
                "Andaman and Nicobar Islands",
                "Andaman e-Seva",
                "https://anieseva.andaman.gov.in/"
        ));

        portals.add(new StatePortal(
                "Chandigarh",
                "Chandigarh e-District",
                "https://eservices.chd.gov.in/"
        ));

        portals.add(new StatePortal(
                "Dadra and Nagar Haveli and Daman and Diu",
                "Single Window Portal",
                "https://swp.dddgov.in/"
        ));

        portals.add(new StatePortal(
                "Delhi",
                "Delhi e-District",
                "https://edistrict.delhi.gov.in/"
        ));

        portals.add(new StatePortal(
                "Jammu and Kashmir",
                "e-UNNAT",
                "https://eunnat.jk.gov.in/"
        ));

        portals.add(new StatePortal(
                "Ladakh",
                "Ladakh e-Seva",
                "https://eseva.ladakh.gov.in/"
        ));

        portals.add(new StatePortal(
                "Lakshadweep",
                "Lakshadweep Administration",
                "https://lakshadweep.gov.in/"
        ));

        portals.add(new StatePortal(
                "Puducherry",
                "Puducherry e-District",
                "https://edistrict.py.gov.in/"
        ));

        return portals;
    }


    public static class StatePortal {

        private String state;
        private String portalName;
        private String officialUrl;

        public StatePortal(
                String state,
                String portalName,
                String officialUrl) {

            this.state = state;
            this.portalName = portalName;
            this.officialUrl = officialUrl;
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
    }
}