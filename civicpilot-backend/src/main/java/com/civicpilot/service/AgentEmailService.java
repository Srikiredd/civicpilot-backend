package com.civicpilot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AgentEmailService {

    private final JavaMailSender mailSender;

    @Value("${civicpilot.mail.from}")
    private String fromEmail;

    public AgentEmailService(
            JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    // ============================================================
    // DOCUMENTS REQUIRED
    // ============================================================

    public void sendDocumentsRequiredEmail(
            String email,
            String citizenName,
            String language,
            String applicationNumber,
            String serviceName,
            String documents) {

        String subject =
                "CivicPilot AI - Documents Required - "
                        + serviceName;

        String body =
                buildStatusEmail(
                        citizenName,
                        language,
                        applicationNumber,
                        serviceName,
                        "DOCUMENTS_REQUIRED",
                        "Required documents are listed below.",
                        documents,
                        null);

        sendEmail(
                email,
                subject,
                body);
    }

    // ============================================================
    // READY TO SUBMIT
    // ============================================================

    public void sendReadyToSubmitEmail(
            String email,
            String citizenName,
            String language,
            String applicationNumber,
            String serviceName) {

        String subject =
                "CivicPilot AI - READY_TO_SUBMIT - "
                        + serviceName;

        String body =
                buildStatusEmail(
                        citizenName,
                        language,
                        applicationNumber,
                        serviceName,
                        "READY_TO_SUBMIT",
                        "Your application is ready to be submitted.",
                        null,
                        null);

        sendEmail(
                email,
                subject,
                body);
    }

    // ============================================================
    // SUBMITTED
    // ============================================================

    public void sendSubmittedEmail(
            String email,
            String citizenName,
            String language,
            String applicationNumber,
            String serviceName) {

        String subject =
                "CivicPilot AI - SUBMITTED - "
                        + serviceName;

        String body =
                buildStatusEmail(
                        citizenName,
                        language,
                        applicationNumber,
                        serviceName,
                        "SUBMITTED",
                        "Your application has been submitted.",
                        null,
                        null);

        sendEmail(
                email,
                subject,
                body);
    }

    // ============================================================
    // UNDER REVIEW
    // ============================================================

    public void sendUnderReviewEmail(
            String email,
            String citizenName,
            String language,
            String applicationNumber,
            String serviceName) {

        String subject =
                "CivicPilot AI - UNDER_REVIEW - "
                        + serviceName;

        String body =
                buildStatusEmail(
                        citizenName,
                        language,
                        applicationNumber,
                        serviceName,
                        "UNDER_REVIEW",
                        "Your application is currently under review.",
                        null,
                        null);

        sendEmail(
                email,
                subject,
                body);
    }

    // ============================================================
    // VERIFICATION
    // ============================================================

    public void sendVerificationEmail(
            String email,
            String citizenName,
            String language,
            String applicationNumber,
            String serviceName) {

        String subject =
                "CivicPilot AI - VERIFICATION - "
                        + serviceName;

        String body =
                buildStatusEmail(
                        citizenName,
                        language,
                        applicationNumber,
                        serviceName,
                        "VERIFICATION",
                        "Your application is currently being verified.",
                        null,
                        null);

        sendEmail(
                email,
                subject,
                body);
    }

    // ============================================================
    // APPROVED
    // ============================================================

    public void sendApprovedEmail(
            String email,
            String citizenName,
            String language,
            String applicationNumber,
            String serviceName) {

        String subject =
                "CivicPilot AI - APPROVED - "
                        + serviceName;

        String body =
                buildStatusEmail(
                        citizenName,
                        language,
                        applicationNumber,
                        serviceName,
                        "APPROVED",
                        "Your government service application has been approved by the concerned authority.",
                        null,
                        null);

        sendEmail(
                email,
                subject,
                body);
    }

    // ============================================================
    // COMPLETED
    // ============================================================

    public void sendCompletedEmail(
            String email,
            String citizenName,
            String language,
            String applicationNumber,
            String serviceName,
            String completionUrl) {

        String subject =
                "CivicPilot AI - COMPLETED - "
                        + serviceName;

        String body =
                buildStatusEmail(
                        citizenName,
                        language,
                        applicationNumber,
                        serviceName,
                        "COMPLETED",
                        "Your government service process has been marked as completed.",
                        null,
                        completionUrl);

        sendEmail(
                email,
                subject,
                body);
    }

    // ============================================================
    // BUILD EMAIL
    // ============================================================

    private String buildStatusEmail(
            String citizenName,
            String language,
            String applicationNumber,
            String serviceName,
            String status,
            String remarks,
            String documents,
            String completionUrl) {

        String normalizedLanguage =
                normalizeLanguage(language);

        if ("Telugu".equalsIgnoreCase(
                normalizedLanguage)) {

            return buildTeluguEmail(
                    citizenName,
                    applicationNumber,
                    serviceName,
                    status,
                    remarks,
                    documents,
                    completionUrl);
        }

        if ("Hindi".equalsIgnoreCase(
                normalizedLanguage)) {

            return buildHindiEmail(
                    citizenName,
                    applicationNumber,
                    serviceName,
                    status,
                    remarks,
                    documents,
                    completionUrl);
        }

        return buildEnglishEmail(
                citizenName,
                applicationNumber,
                serviceName,
                status,
                remarks,
                documents,
                completionUrl);
    }

    // ============================================================
    // ENGLISH
    // ============================================================

    private String buildEnglishEmail(
            String citizenName,
            String applicationNumber,
            String serviceName,
            String status,
            String remarks,
            String documents,
            String completionUrl) {

        StringBuilder body =
                new StringBuilder();

        body.append("Dear ")
                .append(safe(citizenName))
                .append(",\n\n");

        body.append(
                "Your CivicPilot AI application has been updated.\n\n");

        body.append(
                "Application Number: ")
                .append(safe(applicationNumber))
                .append("\n");

        body.append(
                "Service: ")
                .append(safe(serviceName))
                .append("\n");

        body.append(
                "Status: ")
                .append(status)
                .append("\n\n");

        body.append(
                "Remarks: ")
                .append(safe(remarks))
                .append("\n\n");

        if ("DOCUMENTS_REQUIRED".equals(status)) {

            body.append(
                    "Required Documents:\n");

            body.append(
                    safe(documents))
                    .append("\n");
        }

        if ("APPROVED".equals(status)) {

            body.append(
                    "Your government service application has been approved by the concerned authority.\n\n");
        }

        if ("COMPLETED".equals(status)) {

            body.append(
                    "Congratulations!\n\n");

            body.append(
                    "Your government service process has been marked as completed.\n\n");

            body.append(
                    "Please retain your application details for future reference.\n\n");

            if (completionUrl != null
                    && !completionUrl.isBlank()) {

                body.append(
                        "Government Portal / Completion Link:\n");

                body.append(
                        completionUrl)
                        .append("\n\n");
            }
        }

        body.append(
                "Thank you for using CivicPilot AI.\n\n");

        body.append(
                "CivicPilot AI\n");

        body.append(
                "From Problem to Resolution through Intelligent Visual Guidance.");

        return body.toString();
    }

    // ============================================================
    // TELUGU
    // ============================================================

    private String buildTeluguEmail(
            String citizenName,
            String applicationNumber,
            String serviceName,
            String status,
            String remarks,
            String documents,
            String completionUrl) {

        StringBuilder body =
                new StringBuilder();

        body.append("ప్రియమైన ")
                .append(safe(citizenName))
                .append(",\n\n");

        body.append(
                "మీ CivicPilot AI దరఖాస్తు నవీకరించబడింది.\n\n");

        body.append(
                "దరఖాస్తు సంఖ్య: ")
                .append(safe(applicationNumber))
                .append("\n");

        body.append(
                "సేవ: ")
                .append(safe(serviceName))
                .append("\n");

        body.append(
                "స్థితి: ")
                .append(status)
                .append("\n\n");

        body.append(
                "గమనికలు: ")
                .append(safe(remarks))
                .append("\n\n");

        if ("DOCUMENTS_REQUIRED".equals(status)) {

            body.append(
                    "అవసరమైన పత్రాలు:\n");

            body.append(
                    safe(documents))
                    .append("\n");
        }

        if ("APPROVED".equals(status)) {

            body.append(
                    "మీ ప్రభుత్వ సేవ దరఖాస్తు సంబంధిత అధికారిచే ఆమోదించబడింది.\n\n");
        }

        if ("COMPLETED".equals(status)) {

            body.append(
                    "అభినందనలు!\n\n");

            body.append(
                    "మీ ప్రభుత్వ సేవ ప్రక్రియ పూర్తయింది.\n\n");

            body.append(
                    "భవిష్యత్తు కోసం మీ దరఖాస్తు వివరాలను భద్రపరచుకోండి.\n\n");

            if (completionUrl != null
                    && !completionUrl.isBlank()) {

                body.append(
                        "ప్రభుత్వ పోర్టల్ / పూర్తి లింక్:\n");

                body.append(
                        completionUrl)
                        .append("\n\n");
            }
        }

        body.append(
                "CivicPilot AI ఉపయోగించినందుకు ధన్యవాదాలు.\n\n");

        body.append(
                "CivicPilot AI\n");

        body.append(
                "From Problem to Resolution through Intelligent Visual Guidance.");

        return body.toString();
    }

    // ============================================================
    // HINDI
    // ============================================================

    private String buildHindiEmail(
            String citizenName,
            String applicationNumber,
            String serviceName,
            String status,
            String remarks,
            String documents,
            String completionUrl) {

        StringBuilder body =
                new StringBuilder();

        body.append("प्रिय ")
                .append(safe(citizenName))
                .append(",\n\n");

        body.append(
                "आपका CivicPilot AI आवेदन अपडेट किया गया है।\n\n");

        body.append(
                "आवेदन संख्या: ")
                .append(safe(applicationNumber))
                .append("\n");

        body.append(
                "सेवा: ")
                .append(safe(serviceName))
                .append("\n");

        body.append(
                "स्थिति: ")
                .append(status)
                .append("\n\n");

        body.append(
                "टिप्पणी: ")
                .append(safe(remarks))
                .append("\n\n");

        if ("DOCUMENTS_REQUIRED".equals(status)) {

            body.append(
                    "आवश्यक दस्तावेज़:\n");

            body.append(
                    safe(documents))
                    .append("\n");
        }

        if ("APPROVED".equals(status)) {

            body.append(
                    "आपका सरकारी सेवा आवेदन संबंधित अधिकारी द्वारा स्वीकृत कर दिया गया है।\n\n");
        }

        if ("COMPLETED".equals(status)) {

            body.append(
                    "बधाई हो!\n\n");

            body.append(
                    "आपकी सरकारी सेवा प्रक्रिया पूर्ण हो गई है।\n\n");

            body.append(
                    "कृपया अपने आवेदन विवरण भविष्य के लिए सुरक्षित रखें।\n\n");

            if (completionUrl != null
                    && !completionUrl.isBlank()) {

                body.append(
                        "सरकारी पोर्टल / पूर्ण लिंक:\n");

                body.append(
                        completionUrl)
                        .append("\n\n");
            }
        }

        body.append(
                "CivicPilot AI का उपयोग करने के लिए धन्यवाद।\n\n");

        body.append(
                "CivicPilot AI\n");

        body.append(
                "From Problem to Resolution through Intelligent Visual Guidance.");

        return body.toString();
    }

    // ============================================================
    // SEND
    // ============================================================

    private void sendEmail(
            String email,
            String subject,
            String body) {

        if (email == null
                || email.isBlank()) {

            throw new IllegalArgumentException(
                    "Citizen email cannot be empty");
        }

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(fromEmail);

        message.setTo(email);

        message.setSubject(subject);

        message.setText(body);

        mailSender.send(message);
    }

    // ============================================================
    // LANGUAGE
    // ============================================================

    private String normalizeLanguage(
            String language) {

        if (language == null
                || language.isBlank()) {

            return "English";
        }

        String value =
                language
                        .trim()
                        .toLowerCase();

        if (value.startsWith("tel")) {
            return "Telugu";
        }

        if (value.startsWith("hin")) {
            return "Hindi";
        }

        return "English";
    }

    // ============================================================
    // SAFE VALUE
    // ============================================================

    private String safe(String value) {

        if (value == null
                || value.isBlank()) {

            return "Not available";
        }

        return value;
    }
}