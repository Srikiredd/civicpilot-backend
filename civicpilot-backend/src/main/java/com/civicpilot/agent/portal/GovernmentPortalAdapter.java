package com.civicpilot.agent.portal;

import java.util.List;

import com.civicpilot.entity.AgentDocument;
import com.civicpilot.entity.AgentRun;
import com.civicpilot.entity.AgentStepExecution;
import com.civicpilot.entity.JourneyStep;

public interface GovernmentPortalAdapter {

    boolean supports(JourneyStep journeyStep);

    PortalExecutionResult execute(
            AgentRun run,
            AgentStepExecution execution,
            List<AgentDocument> documents);
}