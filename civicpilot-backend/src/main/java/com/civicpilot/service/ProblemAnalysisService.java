package com.civicpilot.service;

import java.util.Collections;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.civicpilot.dto.AiProblemAnalysis;
import com.civicpilot.dto.AnalyzeProblemRequest;
import com.civicpilot.dto.ProblemAnalysisResponse;
import com.civicpilot.entity.ProblemAnalysis;
import com.civicpilot.entity.User;
import com.civicpilot.repository.ProblemAnalysisRepository;

@Service
public class ProblemAnalysisService {

    private final ProblemAnalysisRepository problemAnalysisRepository;

    private final ChatClient chatClient;

    public ProblemAnalysisService(
            ProblemAnalysisRepository problemAnalysisRepository,
            ChatClient.Builder chatClientBuilder) {

        this.problemAnalysisRepository =
                problemAnalysisRepository;

        this.chatClient =
                chatClientBuilder.build();
    }

    public ProblemAnalysisResponse analyzeProblem(
            AnalyzeProblemRequest request,
            User user) {

        String prompt = """
                You are CivicPilot AI, an intelligent government service
                assistance system.

                Analyze the citizen's real-life problem.

                Identify:

                1. The main life event or situation.
                2. The government services or assistance the citizen may need.

                Important rules:

                - Understand the meaning of the complete problem.
                - Do not depend only on exact keywords.
                - Consider related services that logically follow from
                  the citizen's situation.
                - Return only services that are reasonably relevant.
                - Do not invent specific government schemes unless clearly
                  justified by the problem.
                - Use clear service names.
                - The response must contain only the requested structured
                  fields.

                Citizen language:
                %s

                Citizen problem:
                %s
                """.formatted(
                        request.getLanguage(),
                        request.getProblemDescription()
                );

        AiProblemAnalysis aiAnalysis =
                chatClient
                        .prompt()
                        .user(prompt)
                        .call()
                        .entity(AiProblemAnalysis.class);

        String lifeEvent =
                aiAnalysis.getLifeEvent();

        List<String> detectedNeeds =
                aiAnalysis.getDetectedNeeds();

        if (detectedNeeds == null) {
            detectedNeeds = Collections.emptyList();
        }

        ProblemAnalysis analysis =
                new ProblemAnalysis();

        analysis.setUser(user);

        analysis.setProblemDescription(
                request.getProblemDescription()
        );

        analysis.setLanguage(
                request.getLanguage()
        );

        analysis.setLifeEvent(
                lifeEvent
        );

        analysis.setDetectedNeeds(
                String.join(",", detectedNeeds)
        );

        ProblemAnalysis savedAnalysis =
                problemAnalysisRepository.save(analysis);

        return new ProblemAnalysisResponse(
                savedAnalysis.getId(),
                savedAnalysis.getProblemDescription(),
                savedAnalysis.getLanguage(),
                savedAnalysis.getLifeEvent(),
                detectedNeeds,
                savedAnalysis.getCreatedAt()
        );
    }
}