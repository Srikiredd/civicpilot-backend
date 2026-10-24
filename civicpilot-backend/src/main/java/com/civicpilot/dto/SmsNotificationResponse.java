package com.civicpilot.dto;

import java.time.LocalDateTime;

import com.civicpilot.entity.SmsNotification;

public class SmsNotificationResponse {

    private Long id;
    private String recipientPhone;
    private String notificationType;
    private String applicationNumber;
    private String status;
    private String providerMessageSid;
    private String errorMessage;
    private LocalDateTime createdAt;

    public SmsNotificationResponse(SmsNotification sms) {
        this.id = sms.getId();
        this.recipientPhone = sms.getRecipientPhone();
        this.notificationType = sms.getNotificationType();
        this.applicationNumber = sms.getApplicationNumber();
        this.status = sms.getStatus();
        this.providerMessageSid = sms.getProviderMessageSid();
        this.errorMessage = sms.getErrorMessage();
        this.createdAt = sms.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getProviderMessageSid() {
        return providerMessageSid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}