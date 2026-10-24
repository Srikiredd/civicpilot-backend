package com.civicpilot.dto;

public class AiVisualContent {

    private Integer stepNumber;
    private Long serviceId;
    private String serviceName;
    private String title;
    private String description;
    private String imagePrompt;

    public AiVisualContent() {
    }

    public AiVisualContent(
            Integer stepNumber,
            Long serviceId,
            String serviceName,
            String title,
            String description,
            String imagePrompt) {

        this.stepNumber = stepNumber;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.title = title;
        this.description = description;
        this.imagePrompt = imagePrompt;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePrompt() {
        return imagePrompt;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePrompt(String imagePrompt) {
        this.imagePrompt = imagePrompt;
    }
}