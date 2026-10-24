package com.civicpilot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpilot.entity.Application;
import com.civicpilot.entity.ApplicationTrackingHistory;

public interface ApplicationTrackingHistoryRepository
        extends JpaRepository<ApplicationTrackingHistory, Long> {

    List<ApplicationTrackingHistory>
    findByApplicationOrderByUpdatedAtAsc(
            Application application);
}