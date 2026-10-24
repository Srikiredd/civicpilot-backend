package com.civicpilot.dto;

public class RequiredDocumentResponse {

    private String documentType;

    private boolean uploaded;

    public RequiredDocumentResponse() {
    }

    public RequiredDocumentResponse(
            String documentType,
            boolean uploaded) {

        this.documentType = documentType;
        this.uploaded = uploaded;
    }

    public String getDocumentType() {
        return documentType;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}