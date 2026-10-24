package com.civicpilot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.civicpilot.dto.MatchedServiceResponse;
import com.civicpilot.entity.ProblemAnalysis;
import com.civicpilot.entity.User;
import com.civicpilot.repository.ProblemAnalysisRepository;

@Service
public class ServiceMatchingService {

    private final ProblemAnalysisRepository problemAnalysisRepository;
    private final VectorStore vectorStore;

    public ServiceMatchingService(
            ProblemAnalysisRepository problemAnalysisRepository,
            VectorStore vectorStore) {

        this.problemAnalysisRepository =
                problemAnalysisRepository;

        this.vectorStore = vectorStore;
    }

    public List<MatchedServiceResponse> matchServices(
            Long analysisId,
            User user) {

        ProblemAnalysis analysis =
                problemAnalysisRepository
                        .findByIdAndUser(analysisId, user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Problem analysis not found"));

        String detectedNeeds =
                analysis.getDetectedNeeds();

        if (detectedNeeds == null ||
                detectedNeeds.isBlank()) {

            return new ArrayList<>();
        }

        List<MatchedServiceResponse> matchedServices =
                new ArrayList<>();

        String[] needs =
                detectedNeeds.split(",");

        for (String need : needs) {

            String query = need.trim();

            if (query.isBlank()) {
                continue;
            }

            List<Document> documents =
                    vectorStore.similaritySearch(query);

            if (documents == null ||
                    documents.isEmpty()) {

                continue;
            }

            Document document =
                    documents.get(0);

            Map<String, Object> metadata =
                    document.getMetadata();

            Long serviceId =
                    ((Number) metadata.get("serviceId"))
                            .longValue();

            String serviceName =
                    (String) metadata.get("serviceName");

            String text =
                    document.getText();

            MatchedServiceResponse response =
                    new MatchedServiceResponse(
                            serviceId,
                            serviceName,
                            text,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "Semantically matched with citizen need: "
                                    + query
                    );

            matchedServices.add(response);
        }

        return matchedServices;
    }
}