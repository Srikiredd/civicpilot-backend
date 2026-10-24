package com.civicpilot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.civicpilot.dto.ApplicationResponse;
import com.civicpilot.dto.ApplicationTrackingHistoryResponse;
import com.civicpilot.dto.CreateApplicationRequest;
import com.civicpilot.dto.UpdateApplicationStatusRequest;
import com.civicpilot.entity.Application;
import com.civicpilot.entity.ApplicationStatus;
import com.civicpilot.entity.ApplicationTrackingHistory;
import com.civicpilot.entity.CitizenJourney;
import com.civicpilot.entity.JourneyStep;
import com.civicpilot.entity.User;
import com.civicpilot.repository.ApplicationRepository;
import com.civicpilot.repository.ApplicationTrackingHistoryRepository;
import com.civicpilot.repository.CitizenJourneyRepository;
import com.civicpilot.repository.JourneyStepRepository;

@Service
public class ApplicationTrackingService {

    private final ApplicationRepository applicationRepository;

    private final ApplicationTrackingHistoryRepository
            historyRepository;

    private final JourneyStepRepository
            journeyStepRepository;

    private final CitizenJourneyRepository
            citizenJourneyRepository;

    private final CommunicationService
            communicationService;

    public ApplicationTrackingService(

            ApplicationRepository applicationRepository,

            ApplicationTrackingHistoryRepository historyRepository,

            JourneyStepRepository journeyStepRepository,

            CitizenJourneyRepository citizenJourneyRepository,

            CommunicationService communicationService) {

        this.applicationRepository =
                applicationRepository;

        this.historyRepository =
                historyRepository;

        this.journeyStepRepository =
                journeyStepRepository;

        this.citizenJourneyRepository =
                citizenJourneyRepository;

        this.communicationService =
                communicationService;
    }

    public ApplicationResponse createApplication(

            CreateApplicationRequest request,

            User user) {

        JourneyStep step =
                journeyStepRepository
                        .findById(request.getStepId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Journey step not found"));

        CitizenJourney journey =
                citizenJourneyRepository
                        .findByIdAndUser(
                                step.getJourney().getId(),
                                user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Journey not found"));

        if (applicationRepository
                .existsByJourneyStepId(step.getId())) {

            throw new RuntimeException(
                    "Application already exists for this journey step");
        }

        Application application =
                new Application();

        application.setUser(user);

        application.setJourney(journey);

        application.setJourneyStep(step);

        application.setServiceName(
                step.getGovernmentService().getName());

        application.setApplicationNumber(
                generateApplicationNumber());

        application.setStatus(
                ApplicationStatus.READY_TO_SUBMIT);

        application.setRemarks(
                "Application created and ready for submission.");

        Application saved =
                applicationRepository.save(application);

        saveHistory(
                saved,
                ApplicationStatus.READY_TO_SUBMIT,
                "Application created and ready for submission.");

        return convertToResponse(saved);
    }

    public ApplicationResponse updateStatus(

            Long applicationId,

            UpdateApplicationStatusRequest request,

            User user) {

        Application application =
                applicationRepository
                        .findByIdAndUser(
                                applicationId,
                                user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        ApplicationStatus newStatus =
                request.getStatus();

        application.setStatus(newStatus);

        application.setRemarks(
                request.getRemarks());

        if (newStatus == ApplicationStatus.SUBMITTED
                && application.getSubmittedAt() == null) {

            application.setSubmittedAt(
                    LocalDateTime.now());
        }

        Application saved =
                applicationRepository.save(application);

        saveHistory(
                saved,
                newStatus,
                request.getRemarks());

        // ==========================================
        // PHASE 10
        // SEND EMAIL + SMS
        // ==========================================

        communicationService
                .sendApplicationStatusCommunication(saved);

        return convertToResponse(saved);
    }

    public ApplicationResponse getApplication(

            Long applicationId,

            User user) {

        Application application =
                applicationRepository
                        .findByIdAndUser(
                                applicationId,
                                user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        return convertToResponse(application);
    }

    public List<ApplicationResponse>
    getAllApplications(User user) {

        List<Application> applications =
                applicationRepository
                        .findByUser(user);

        List<ApplicationResponse> responses =
                new ArrayList<>();

        for (Application application
                : applications) {

            responses.add(
                    convertToResponse(application));
        }

        return responses;
    }

    private void saveHistory(

            Application application,

            ApplicationStatus status,

            String remarks) {

        ApplicationTrackingHistory history =
                new ApplicationTrackingHistory();

        history.setApplication(application);

        history.setStatus(status);

        history.setRemarks(remarks);

        historyRepository.save(history);
    }

    private ApplicationResponse convertToResponse(

            Application application) {

        List<ApplicationTrackingHistory>
                historyList =
                historyRepository
                        .findByApplicationOrderByUpdatedAtAsc(
                                application);

        List<ApplicationTrackingHistoryResponse>
                historyResponses =
                new ArrayList<>();

        for (ApplicationTrackingHistory history
                : historyList) {

            historyResponses.add(
                    new ApplicationTrackingHistoryResponse(

                            history.getStatus(),

                            history.getRemarks(),

                            history.getUpdatedAt()));
        }

        return new ApplicationResponse(

                application.getId(),

                application.getJourney().getId(),

                application.getJourneyStep().getId(),

                application.getApplicationNumber(),

                application.getServiceName(),

                application.getStatus(),

                application.getRemarks(),

                application.getSubmittedAt(),

                application.getLastUpdatedAt(),

                application.getCreatedAt(),

                historyResponses);
    }

    private String generateApplicationNumber() {

        return "CP-" + System.currentTimeMillis();
    }
}