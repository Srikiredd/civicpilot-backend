package com.civicpilot.agent.browser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AgentRuntimeFileStore {

    private final Path root =
            Paths.get("agent-runtime-documents");

    public Path save(
            Long runId,
            Long stepExecutionId,
            String documentType,
            MultipartFile file) {

        try {

            Path directory =
                    root.resolve(String.valueOf(runId))
                            .resolve(String.valueOf(stepExecutionId));

            Files.createDirectories(directory);

            String safeType =
                    documentType.replaceAll(
                            "[^a-zA-Z0-9._-]",
                            "_");

            String safeName =
                    file.getOriginalFilename() == null
                            ? "document.bin"
                            : file.getOriginalFilename()
                                    .replaceAll(
                                            "[^a-zA-Z0-9._-]",
                                            "_");

            Path target =
                    directory.resolve(
                            safeType
                                    + "__"
                                    + safeName);

            file.transferTo(target);

            return target.toAbsolutePath();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to store Agent runtime document",
                    e);
        }
    }

    public Map<String, String> files(
            Long runId,
            Long stepExecutionId) {

        Map<String, String> result =
                new LinkedHashMap<>();

        Path directory =
                root.resolve(String.valueOf(runId))
                        .resolve(String.valueOf(stepExecutionId));

        if (!Files.exists(directory)) {
            return result;
        }

        try (var stream =
                     Files.list(directory)) {

            stream.filter(Files::isRegularFile)
                    .forEach(path -> {

                        String name =
                                path.getFileName()
                                        .toString();

                        int split =
                                name.indexOf("__");

                        String documentType =
                                split > 0
                                        ? name.substring(
                                                0,
                                                split)
                                        : name;

                        result.put(
                                documentType,
                                path.toAbsolutePath()
                                        .toString());
                    });

            return result;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to read Agent runtime documents",
                    e);
        }
    }
}