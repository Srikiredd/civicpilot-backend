
package com.civicpilot.service;

import org.springframework.stereotype.Service;

import com.civicpilot.entity.Application;

@Service
public class CommunicationService {

    private final EmailService emailService;
    private final SmsService smsService;

    public CommunicationService(
            EmailService emailService,
            SmsService smsService) {

        this.emailService = emailService;
        this.smsService = smsService;
    }

    public void sendApplicationStatusCommunication(
            Application application) {

        String status = application.getStatus().name();

        // Send Email
        try {
            emailService.sendApplicationStatusEmail(
                    application,
                    status
            );

            System.out.println(
                    "Email sent successfully for application: "
                    + application.getApplicationNumber()
            );

        } catch (Exception e) {
            System.err.println(
                    "Email sending failed: " + e.getMessage()
            );
        }

        // Send SMS
        try {
            String smsSid = smsService.sendApplicationStatusSms(
                    application,
                    status
            );

            System.out.println(
                    "SMS sent successfully. SID: " + smsSid
            );

        } catch (Exception e) {
            System.err.println(
                    "SMS sending failed: " + e.getMessage()
            );
        }
    }
}
