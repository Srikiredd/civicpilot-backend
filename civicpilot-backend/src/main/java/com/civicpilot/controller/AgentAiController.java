package com.civicpilot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.civicpilot.dto.AgentAiDecision;
import com.civicpilot.entity.AgentDocument;
import com.civicpilot.entity.AgentRun;
import com.civicpilot.entity.AgentStepExecution;
import com.civicpilot.repository.AgentDocumentRepository;
import com.civicpilot.repository.AgentRunRepository;
import com.civicpilot.repository.AgentStepExecutionRepository;
import com.civicpilot.service.AgentAiDecisionService;

import java.util.List;

@RestController
@RequestMapping("/api/agent/ai")
public class AgentAiController {

    private final AgentAiDecisionService aiDecisionService;
    private final AgentRunRepository agentRunRepository;
    private final AgentStepExecutionRepository stepExecutionRepository;
    private final AgentDocumentRepository documentRepository;

    public AgentAiController(
            AgentAiDecisionService aiDecisionService,
            AgentRunRepository agentRunRepository,
            AgentStepExecutionRepository stepExecutionRepository,
            AgentDocumentRepository documentRepository) {

        this.aiDecisionService =
                aiDecisionService;

        this.agentRunRepository =
                agentRunRepository;

        this.stepExecutionRepository =
                stepExecutionRepository;

        this.documentRepository =
                documentRepository;
    }

    @GetMapping(
            "/runs/{runId}/steps/{stepExecutionId}/analyze")
    public ResponseEntity<AgentAiDecision> analyze(
            @PathVariable Long runId,
            @PathVariable Long stepExecutionId) {

        AgentRun run =
                agentRunRepository.findById(runId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent run not found"));

        AgentStepExecution execution =
                stepExecutionRepository.findById(
                        stepExecutionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Agent step execution not found"));

        if (!execution
                .getAgentRun()
                .getId()
                .equals(run.getId())) {

            throw new RuntimeException(
                    "Agent step does not belong to this run");
        }

        List<AgentDocument> documents =
                documentRepository
                        .findByStepExecutionId(
                                stepExecutionId);

        AgentAiDecision decision =
                aiDecisionService.decide(
                        execution,
                        documents,
                        run.getLanguage());

        return ResponseEntity.ok(decision);
    }
}