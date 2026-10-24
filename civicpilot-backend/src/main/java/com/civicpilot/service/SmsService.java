
package com.civicpilot.service;

import com.civicpilot.entity.Application;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @Value("${twilio.test-recipient}")
    private String testRecipient;

    private volatile boolean initialized = false;

    private synchronized void initializeTwilio() {
        if (!initialized) {
            Twilio.init(accountSid, authToken);
            initialized = true;
        }
    }

    public String sendSms(String to, String messageBody) {

        if (to == null || !to.matches("^\\+[1-9]\\d{7,14}$")) {
            throw new IllegalArgumentException(
                    "Recipient must use international format, "
                    + "for example +91XXXXXXXXXX"
            );
        }

        if (messageBody == null || messageBody.isBlank()) {
            throw new IllegalArgumentException(
                    "SMS message cannot be empty"
            );
        }

        initializeTwilio();

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(fromPhoneNumber),
                messageBody
        ).create();

        return message.getSid();
    }

    // Called by CommunicationService
    public String sendApplicationStatusSms(
            Application application,
            String status) {

        String messageBody = buildStatusMessage(
                application.getApplicationNumber(),
                status,
                "english",
                null,
                null
        );

        return sendSms(testRecipient, messageBody);
    }

    // Multilingual SMS method
    public String sendStatusSms(
            String phoneNumber,
            String serviceName,
            String applicationNumber,
            String status,
            String portalUrl,
            String language) {

        String messageBody = buildStatusMessage(
                applicationNumber,
                status,
                language,
                serviceName,
                portalUrl
        );

        return sendSms(phoneNumber, messageBody);
    }

    private String buildStatusMessage(
            String applicationNumber,
            String status,
            String language,
            String serviceName,
            String portalUrl) {

        String lang = language == null
                ? "english"
                : language.trim().toLowerCase();

        String service = safe(serviceName);
        String appNumber = safe(applicationNumber);
        String currentStatus = safe(status);

        String message;

        if (lang.startsWith("tel")) {

            message = "CivicPilot AI: మీ "
                    + service
                    + " దరఖాస్తు స్థితి: "
                    + currentStatus
                    + ". దరఖాస్తు సంఖ్య: "
                    + appNumber + ".";

        } else if (lang.startsWith("hin")) {

            message = "CivicPilot AI: आपकी "
                    + service
                    + " सेवा की स्थिति: "
                    + currentStatus
                    + ". आवेदन संख्या: "
                    + appNumber + ".";

        } else {

            message = "CivicPilot AI: Your "
                    + service
                    + " application status is "
                    + currentStatus
                    + ". Application No: "
                    + appNumber + ".";
        }

        if (portalUrl != null && !portalUrl.isBlank()) {
            message += " Portal: " + portalUrl;
        }

        return message;
    }

    private String safe(String value) {
        return value == null || value.isBlank()
                ? "Not available"
                : value;
    }
}
