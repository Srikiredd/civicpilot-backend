package com.civicpilot.agent.browser;

import java.nio.file.Paths;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BrowserAgentTools {

    private final Page page;

    private final String allowedHost;

    private final Map<String, String> documents;

    private boolean humanActionRequired;

    private String humanActionReason;

    public BrowserAgentTools(
            Page page,
            String allowedHost,
            Map<String, String> documents) {

        this.page = page;
        this.allowedHost = allowedHost;
        this.documents = documents;
    }

    @Tool(
        description =
            "Inspect the current government webpage. "
            + "Returns URL, title, visible text, forms, "
            + "buttons and inputs. Call this before choosing selectors."
    )
    public String inspectPage() {

        return "URL="
                + page.url()

                + "\nTITLE="
                + page.title()

                + "\nPAGE_TEXT=\n"
                + limit(
                        page.locator("body")
                                .innerText(),
                        14000)

                + "\nINPUTS=\n"
                + describeInputs()

                + "\nBUTTONS=\n"
                + describeButtons();
    }

    @Tool(
        description =
            "Navigate to an official government URL. "
            + "Only the configured official government host "
            + "or a gov.in/nic.in host is permitted."
    )
    public String navigate(String url) {

        if (!allowed(url)) {

            return
                "BLOCKED: navigation outside the "
                + "approved government domain is not allowed.";
        }

        page.navigate(url);

        page.waitForLoadState();

        return
                "NAVIGATED: "
                + page.url();
    }

    @Tool(
        description =
            "Fill a normal non-sensitive form field "
            + "using a CSS selector and value."
    )
    public String fill(
            String selector,
            String value) {

        Locator locator =
                page.locator(selector)
                        .first();

        String metadata =
                metadata(locator);

        if (sensitive(metadata)) {

            return human(
                    "The field appears to require "
                    + "OTP, CAPTCHA, biometric, signature, "
                    + "payment or another human-only action: "
                    + metadata);
        }

        locator.fill(value);

        return
                "FILLED: "
                + selector;
    }

    @Tool(
        description =
            "Select an option in a normal HTML select element."
    )
    public String selectOption(
            String selector,
            String value) {

        Locator locator =
                page.locator(selector)
                        .first();

        String metadata =
                metadata(locator);

        if (sensitive(metadata)) {

            return human(
                    "The select appears to be "
                    + "associated with a human-only action: "
                    + metadata);
        }

        locator.selectOption(value);

        return
                "SELECTED: "
                + selector
                + " = "
                + value;
    }

    @Tool(
        description =
            "Upload one previously supplied citizen document "
            + "to a file input. documentType must match "
            + "one of the supplied documents."
    )
    public String uploadDocument(
            String selector,
            String documentType) {

        String path =
                findDocument(documentType);

        if (path == null) {

            return
                    "ERROR: document not found for type "
                    + documentType;
        }

        Locator locator =
                page.locator(selector)
                        .first();

        locator.setInputFiles(
                Paths.get(path));

        return
                "UPLOADED: "
                + documentType;
    }

    @Tool(
        description =
            "Click a normal page element using CSS selector. "
            + "Do not click OTP, CAPTCHA, biometric, "
            + "digital signature or payment controls."
    )
    public String click(String selector) {

        Locator locator =
                page.locator(selector)
                        .first();

        String metadata =
                metadata(locator);

        if (sensitive(metadata)) {

            return human(
                    "The requested control is human-only: "
                    + metadata);
        }

        locator.click();

        page.waitForLoadState();

        return
                "CLICKED: "
                + selector
                + "\nURL="
                + page.url();
    }

    @Tool(
        description =
            "Press a keyboard key on a normal field."
    )
    public String press(
            String selector,
            String key) {

        Locator locator =
                page.locator(selector)
                        .first();

        String metadata =
                metadata(locator);

        if (sensitive(metadata)) {

            return human(
                    "The requested field is human-only: "
                    + metadata);
        }

        locator.press(key);

        return
                "PRESSED: "
                + key;
    }

    @Tool(
        description =
            "Wait for a short period after a browser action."
    )
    public String waitFor(
            int milliseconds) {

        int safe =
                Math.max(
                        100,
                        Math.min(
                                milliseconds,
                                10000));

        page.waitForTimeout(safe);

        return
                "WAITED: "
                + safe
                + " ms";
    }

    @Tool(
        description =
            "Stop automation and request human action. "
            + "Use for OTP, CAPTCHA, biometric authentication, "
            + "digital signature, payment authorization or "
            + "another legally required human action."
    )
    public String requestHumanAction(
            String reason) {

        return human(reason);
    }

    public boolean isHumanActionRequired() {

        return humanActionRequired;
    }

    public String getHumanActionReason() {

        return humanActionReason;
    }

    private String human(
            String reason) {

        humanActionRequired = true;

        humanActionReason = reason;

        return
                "HUMAN_ACTION_REQUIRED: "
                + reason;
    }

    private boolean allowed(
            String url) {

        try {

            java.net.URI uri =
                    java.net.URI.create(url);

            String host =
                    uri.getHost();

            return host != null
                    && (
                        host.equalsIgnoreCase(
                                allowedHost)

                        || host.endsWith(
                                ".gov.in")

                        || host.endsWith(
                                ".nic.in")
                    );

        } catch (Exception e) {

            return false;
        }
    }

    private String findDocument(
            String requested) {

        String normalized =
                normalize(requested);

        for (Map.Entry<String, String> entry
                : documents.entrySet()) {

            if (normalize(entry.getKey())
                    .equals(normalized)) {

                return entry.getValue();
            }
        }

        return null;
    }

    private String metadata(
            Locator locator) {

        try {

            Object value =
                    locator.evaluate(
                        "el => JSON.stringify({"
                        + "tag:el.tagName,"
                        + "name:el.getAttribute('name'),"
                        + "id:el.id,"
                        + "type:el.getAttribute('type'),"
                        + "aria:el.getAttribute('aria-label'),"
                        + "placeholder:el.getAttribute('placeholder'),"
                        + "text:el.innerText ? el.innerText : el.value"
                        + "})"
                    );

            return String.valueOf(value);

        } catch (Exception e) {

            return "unknown-element";
        }
    }

    private boolean sensitive(
            String value) {

        String v =
                value.toLowerCase();

        return
                v.contains("otp")
                || v.contains("one time")
                || v.contains("captcha")
                || v.contains("biometric")
                || v.contains("iris")
                || v.contains("fingerprint")
                || v.contains("digital signature")
                || v.contains("e-sign")
                || v.contains("payment")
                || v.contains("upi")
                || v.contains("card number")
                || v.contains("cvv")
                || v.contains("password");
    }

    private String describeInputs() {

        StringBuilder out =
                new StringBuilder();

        for (Locator input :
                page.locator(
                        "input, textarea, select")
                        .all()) {

            out.append(
                    metadata(input))
                    .append('\n');
        }

        return limit(
                out.toString(),
                7000);
    }

    private String describeButtons() {

        StringBuilder out =
                new StringBuilder();

        for (Locator button :
                page.locator(
                        "button, input[type=submit], a")
                        .all()) {

            out.append(
                    metadata(button))
                    .append('\n');
        }

        return limit(
                out.toString(),
                5000);
    }

    private String normalize(
            String value) {

        return value == null
                ? ""
                : value.toLowerCase()
                        .replaceAll(
                                "[^a-z0-9]",
                                "");
    }

    private String limit(
            String value,
            int max) {

        if (value == null) {
            return "";
        }

        return value.length() <= max
                ? value
                : value.substring(
                        0,
                        max)
                        + "...";
    }
}