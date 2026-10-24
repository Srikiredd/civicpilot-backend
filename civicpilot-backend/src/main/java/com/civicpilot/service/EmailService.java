package com.civicpilot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.civicpilot.entity.Application;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendApplicationStatusEmail(
            Application application,
            String status) {

        String recipient = application.getUser().getEmail();

        String subject =
                "CivicPilot AI - Application Status: " + status;

        String message =
                "Dear " + application.getUser().getName() + ",\n\n"
                + "Your CivicPilot AI application has been updated.\n\n"
                + "Application Number: "
                + application.getApplicationNumber() + "\n"
                + "Service: "
                + application.getServiceName() + "\n"
                + "Status: "
                + status + "\n\n"
                + "Remarks: "
                + (application.getRemarks() == null
                    ? "No additional remarks."
                    : application.getRemarks())
                + "\n\n"
                + "Last Updated: "
                + application.getLastUpdatedAt()
                + "\n\n";

        if ("COMPLETED".equals(status)) {

            message +=
                    "Congratulations!\n\n"
                    + "Your government service process has been "
                    + "marked as completed.\n\n"
                    + "Please retain your application details "
                    + "for future reference.\n\n";

        } else if ("APPROVED".equals(status)) {

            message +=
                    "Your application has been approved by "
                    + "the concerned authority.\n\n";

        } else if ("REJECTED".equals(status)) {

            message +=
                    "Your application has been rejected. "
                    + "Please review the remarks and official "
                    + "government instructions before taking "
                    + "further action.\n\n";
        }

        message +=
                "Thank you for using CivicPilot AI.\n\n"
                + "CivicPilot AI\n"
                + "From Problem to Resolution through "
                + "Intelligent Visual Guidance.";

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(senderEmail);
        mail.setTo(recipient);
        mail.setSubject(subject);
        mail.setText(message);

        mailSender.send(mail);
    }
}