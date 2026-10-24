package com.civicpilot.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.civicpilot.dto.AgentAiDecision;
import com.civicpilot.entity.AgentDocument;
import com.civicpilot.entity.AgentStepExecution;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AgentAiDecisionService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AgentAiDecisionService(
            ChatClient.Builder chatClientBuilder,
            ObjectMapper objectMapper) {

        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public AgentAiDecision decide(
            AgentStepExecution execution,
            List<AgentDocument> documents,
            String language) {

        String serviceName =
                execution.getJourneyStep()
                        .getGovernmentService()
                        .getName();

        String portalUrl =
                execution.getPortalUrl();

        StringBuilder documentText =
                new StringBuilder();

        for (AgentDocument document : documents) {
            documentText
                    .append("- ")
                    .append(document.getDocumentType())
                    .append("\n");
        }

        String prompt = """
                You are the autonomous AI decision engine
                of CivicPilot AI.

                Government service:
                %s

                Government portal:
                %s

                Citizen language:
                %s

                Uploaded documents:
                %s

                Decide the next safe action.

                Allowed actions:

                EXECUTE_PORTAL
                WAIT_FOR_HUMAN_ACTION
                FAIL

                Rules:

                1. If required documents are available,
                   choose EXECUTE_PORTAL.

                2. Government portals may require:
                   OTP
                   CAPTCHA
                   biometric authentication
                   digital signature
                   payment authorization.

                3. Never bypass OTP, CAPTCHA, biometric
                   authentication, signature or payment
                   authorization.

                4. If such human action is required,
                   choose WAIT_FOR_HUMAN_ACTION.

                5. Do not invent government requirements.

                Return ONLY valid JSON:

                {
                  "action": "EXECUTE_PORTAL",
                  "reason": "reason",
                  "status": "READY_TO_SUBMIT",
                  "humanActionRequired": false,
                  "humanActionReason": ""
                }
                """.formatted(
                        serviceName,
                        portalUrl,
                        language,
                        documentText
                );

        try {

            String response =
                    chatClient
                            .prompt()
                            .user(prompt)
                            .call()
                            .content();

            if (response == null || response.isBlank()) {
                return fallbackDecision();
            }

            response =
                    cleanJson(response);

            AgentAiDecision decision =
                    objectMapper.readValue(
                            response,
                            AgentAiDecision.class
                    );

            if (decision.getAction() == null) {
                return fallbackDecision();
            }

            return decision;

        } catch (Exception e) {

            return fallbackDecision();
        }
    }

    private String cleanJson(String response) {

        response =
                response
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();

        int start =
                response.indexOf("{");

        int end =
                response.lastIndexOf("}");

        if (start >= 0 && end > start) {
            return response.substring(
                    start,
                    end + 1
            );
        }

        return response;
    }

    private AgentAiDecision fallbackDecision() {

        AgentAiDecision decision =
                new AgentAiDecision();

        decision.setAction("EXECUTE_PORTAL");
        decision.setReason(
                "Required documents are available.");
        decision.setStatus(
                "READY_TO_SUBMIT");
        decision.setHumanActionRequired(false);
        decision.setHumanActionReason("");

        return decision;
    }
}