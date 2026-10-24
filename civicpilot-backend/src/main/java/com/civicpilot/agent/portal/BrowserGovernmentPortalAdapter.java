package com.civicpilot.agent.portal;

import java.util.List;

import org.springframework.stereotype.Component;

import com.civicpilot.agent.browser.RealGovernmentPortalAgent;
import com.civicpilot.entity.AgentDocument;
import com.civicpilot.entity.AgentRun;
import com.civicpilot.entity.AgentStepExecution;
import com.civicpilot.entity.JourneyStep;

@Component
public class BrowserGovernmentPortalAdapter
        implements GovernmentPortalAdapter {

    private final RealGovernmentPortalAgent agent;

    public BrowserGovernmentPortalAdapter(
            RealGovernmentPortalAgent agent) {

        this.agent = agent;
    }

    @Override
    public boolean supports(
            JourneyStep journeyStep) {

        return journeyStep != null
                && journeyStep.getGovernmentService() != null;
    }

    @Override
    public PortalExecutionResult execute(
            AgentRun run,
            AgentStepExecution execution,
            List<AgentDocument> documents) {

        return agent.execute(
                run,
                execution,
                documents);
    }
}