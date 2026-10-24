package com.civicpilot.agent.browser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.civicpilot.agent.portal.PortalExecutionResult;
import com.civicpilot.entity.AgentDocument;
import com.civicpilot.entity.AgentRun;
import com.civicpilot.entity.AgentStepExecution;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@Service
public class RealGovernmentPortalAgent {

    private final ChatClient chatClient;

    private final AgentRuntimeFileStore fileStore;

    public RealGovernmentPortalAgent(
            ChatClient.Builder chatClientBuilder,
            AgentRuntimeFileStore fileStore) {

        this.chatClient =
                chatClientBuilder.build();

        this.fileStore =
                fileStore;
    }

    public PortalExecutionResult execute(
            AgentRun run,
            AgentStepExecution execution,
            List<AgentDocument> documents) {

        String portalUrl =
                execution.getPortalUrl();

        if (portalUrl == null
                || portalUrl.isBlank()) {

            return new PortalExecutionResult(
                    false,
                    false,
                    "No official government portal "
                    + "is configured for this service.",
                    null,
                    null);
        }

        java.net.URI uri =
                java.net.URI.create(
                        portalUrl);

        String host =
                uri.getHost();

        if (host == null
                || !(
                    host.endsWith(".gov.in")
                    || host.endsWith(".nic.in")
                    || host.equalsIgnoreCase("gov.in")
                )) {

            return new PortalExecutionResult(
                    false,
                    false,
                    "Portal URL is not an approved "
                    + "government domain: "
                    + portalUrl,
                    portalUrl,
                    portalUrl);
        }

        Map<String, String> runtimeFiles =
                fileStore.files(
                        run.getId(),
                        execution.getId());

        String serviceName =
                execution
                        .getJourneyStep()
                        .getGovernmentService()
                        .getName();

        String requiredDocuments =
                execution.getRequiredDocuments();

        Path profile =
                Paths.get(
                        "agent-browser-profiles",
                        "run-" + run.getId());

        try {

            Files.createDirectories(
                    profile);

        } catch (Exception e) {

            return new PortalExecutionResult(
                    false,
                    false,
                    "Unable to create browser profile: "
                    + e.getMessage(),
                    portalUrl,
                    portalUrl);
        }

        try (Playwright playwright =
                     Playwright.create()) {

            BrowserType.LaunchPersistentContextOptions options =
                    new BrowserType
                            .LaunchPersistentContextOptions()
                            .setHeadless(false)
                            .setViewportSize(
                                    1440,
                                    900);

            try (BrowserContext context =
                         playwright.chromium()
                                 .launchPersistentContext(
                                         profile,
                                         options)) {

                Page page =
                        context.pages().isEmpty()
                                ? context.newPage()
                                : context.pages().get(0);

                page.navigate(
                        portalUrl);

                page.waitForLoadState();

                BrowserAgentTools tools =
                        new BrowserAgentTools(
                                page,
                                host,
                                runtimeFiles);

                String prompt = """

                        You are CivicPilot AI's
                        autonomous browser agent.

                        You must operate the REAL
                        official government website.

                        DO NOT SIMULATE.
                        DO NOT INVENT.
                        DO NOT CLAIM SUCCESS WITHOUT
                        PERFORMING THE REAL BROWSER ACTION.

                        SERVICE:
                        %s

                        REQUIRED DOCUMENTS:
                        %s

                        AVAILABLE DOCUMENTS:
                        %s

                        OFFICIAL PORTAL:
                        %s

                        RULES:

                        1. Inspect the page before
                           selecting elements.

                        2. Use browser tools to perform
                           real navigation and actions.

                        3. Upload only documents
                           supplied to the agent.

                        4. Never guess OTP.

                        5. Never bypass CAPTCHA.

                        6. Never bypass biometric
                           authentication.

                        7. Never create or guess
                           digital signatures.

                        8. Never authorize a payment.

                        9. If human action is required,
                           immediately call
                           requestHumanAction()
                           and stop.

                        10. Do not navigate outside
                            approved government domains.

                        11. Do not claim submission
                            unless the actual page
                            confirms submission.

                        12. Inspect the page again
                            after important actions.

                        13. If an application number
                            appears, capture it.

                        14. If a real status URL appears,
                            capture it.

                        15. Finish only after a real
                            browser result exists.

                        """.formatted(
                                serviceName,
                                requiredDocuments,
                                runtimeFiles.keySet(),
                                portalUrl);

                String result =
                        chatClient.prompt()

                                .system(
                                    "You are an autonomous "
                                    + "web automation agent. "
                                    + "Use browser tools to "
                                    + "perform actions instead "
                                    + "of merely explaining them.")

                                .user(prompt)

                                .tools(tools)

                                .call()

                                .content();

                if (tools.isHumanActionRequired()) {

                    return new PortalExecutionResult(
                            false,
                            true,
                            tools.getHumanActionReason(),
                            page.url(),
                            page.url());
                }

                String lower =
                        result == null
                                ? ""
                                : result.toLowerCase();

                boolean failed =
                        lower.contains("unable to")
                        || lower.contains("cannot complete")
                        || lower.contains("failed");

                if (failed) {

                    return new PortalExecutionResult(
                            false,
                            false,
                            result,
                            page.url(),
                            page.url());
                }

                return new PortalExecutionResult(
                        true,
                        false,
                        result == null
                                ? "Government portal operation completed."
                                : result,
                        page.url(),
                        page.url());
            }

        } catch (Exception e) {

            return new PortalExecutionResult(
                    false,
                    false,
                    "Real browser agent failed: "
                    + e.getMessage(),
                    portalUrl,
                    portalUrl);
        }
    }
}