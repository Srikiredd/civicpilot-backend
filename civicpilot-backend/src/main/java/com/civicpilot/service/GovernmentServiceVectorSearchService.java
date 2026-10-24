package com.civicpilot.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class GovernmentServiceVectorSearchService {

    private final VectorStore vectorStore;

    public GovernmentServiceVectorSearchService(
            VectorStore vectorStore) {

        this.vectorStore = vectorStore;
    }

    public List<Document> searchServices(String query) {

        return vectorStore.similaritySearch(query);
    }
}