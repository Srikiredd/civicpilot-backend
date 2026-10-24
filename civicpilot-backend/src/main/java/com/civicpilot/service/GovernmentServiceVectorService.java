package com.civicpilot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.civicpilot.entity.GovernmentService;
import com.civicpilot.repository.GovernmentServiceRepository;

@Service
public class GovernmentServiceVectorService {

    private final GovernmentServiceRepository governmentServiceRepository;
    private final VectorStore vectorStore;

    public GovernmentServiceVectorService(
            GovernmentServiceRepository governmentServiceRepository,
            VectorStore vectorStore) {

        this.governmentServiceRepository =
                governmentServiceRepository;

        this.vectorStore = vectorStore;
    }

    public void indexGovernmentServices() {

        List<GovernmentService> services =
                governmentServiceRepository.findAll();

        List<Document> documents = new ArrayList<>();

        for (GovernmentService service : services) {

            String content = """
                    Government Service: %s

                    Description: %s

                    Category: %s

                    Department: %s

                    Eligibility: %s

                    Required Documents: %s

                    Procedure: %s
                    """.formatted(
                    service.getName(),
                    service.getDescription(),
                    service.getCategory(),
                    service.getDepartment(),
                    service.getEligibility(),
                    service.getRequiredDocuments(),
                    service.getProcedureDescription()
            );

            Document document =
                    new Document(
                            content,
                            java.util.Map.of(
                                    "serviceId",
                                    service.getId(),
                                    "serviceName",
                                    service.getName()
                            )
                    );

            documents.add(document);
        }

        if (!documents.isEmpty()) {
            vectorStore.add(documents);
        }
    }
}