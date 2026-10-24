package com.civicpilot.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.civicpilot.entity.AgentDocument;
import com.civicpilot.entity.AgentRun;
import com.civicpilot.entity.AgentStepExecution;
import com.civicpilot.repository.AgentDocumentRepository;

@Service
public class AgentDocumentService {

    private final AgentDocumentRepository documentRepository;

    private final Path root =
            Paths.get("agent-documents");

    public AgentDocumentService(
            AgentDocumentRepository documentRepository) {

        this.documentRepository =
                documentRepository;

        try {

            Files.createDirectories(root);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not create agent document directory",
                    e);
        }
    }

    public AgentDocument saveDocument(
            AgentRun run,
            AgentStepExecution stepExecution,
            String documentType,
            MultipartFile file) {

        validate(file);

        try {

            String extension =
                    getExtension(
                            file.getOriginalFilename());

            String storedFileName =
                    UUID.randomUUID()
                    + extension;

            Path runDirectory =
                    root.resolve(
                            String.valueOf(run.getId()));

            Files.createDirectories(runDirectory);

            Path stepDirectory =
                    runDirectory.resolve(
                            String.valueOf(
                                    stepExecution.getId()));

            Files.createDirectories(stepDirectory);

            Path destination =
                    stepDirectory.resolve(
                            storedFileName);

            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            AgentDocument document =
                    new AgentDocument();

            document.setAgentRun(run);

            document.setStepExecution(
                    stepExecution);

            document.setDocumentType(
                    documentType);

            document.setOriginalFileName(
                    file.getOriginalFilename());

            document.setStoredFileName(
                    destination.toString());

            document.setContentType(
                    file.getContentType());

            document.setFileSize(
                    file.getSize());

            return documentRepository.save(
                    document);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to store document",
                    e);
        }
    }

    private void validate(
            MultipartFile file) {

        if (file == null
                || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Document file is empty");
        }

        long maxSize =
                10L * 1024L * 1024L;

        if (file.getSize() > maxSize) {

            throw new IllegalArgumentException(
                    "Document size cannot exceed 10 MB");
        }

        String contentType =
                file.getContentType();

        if (contentType == null) {

            throw new IllegalArgumentException(
                    "Document content type is missing");
        }

        if (!contentType.equals(
                "application/pdf")

                && !contentType.equals(
                        "image/jpeg")

                && !contentType.equals(
                        "image/png")) {

            throw new IllegalArgumentException(
                    "Only PDF, JPG and PNG documents are supported");
        }
    }

    private String getExtension(
            String fileName) {

        if (fileName == null
                || !fileName.contains(".")) {

            return "";
        }

        return fileName.substring(
                fileName.lastIndexOf("."));
    }
}