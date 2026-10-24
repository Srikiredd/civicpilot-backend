package com.civicpilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.SmsNotification;

public interface SmsNotificationRepository
        extends JpaRepository<SmsNotification, Long> {

    List<SmsNotification> findByRecipientPhoneOrderByCreatedAtDesc(
            String recipientPhone);

    List<SmsNotification> findByApplicationNumberOrderByCreatedAtDesc(
            String applicationNumber);
}