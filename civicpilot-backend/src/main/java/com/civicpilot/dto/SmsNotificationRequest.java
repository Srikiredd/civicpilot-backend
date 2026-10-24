package com.civicpilot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SmsNotificationRequest {

    @NotBlank
    @Pattern(
        regexp = "^\\+[1-9]\\d{7,14}$",
        message = "Phone number must include its country code"
    )
    private String recipientPhone;

    @NotBlank
    @Size(max = 1000)
    private String message;

    @NotBlank
    @Size(max = 40)
    private String notificationType;

    @Size(max = 100)
    private String applicationNumber;

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
}