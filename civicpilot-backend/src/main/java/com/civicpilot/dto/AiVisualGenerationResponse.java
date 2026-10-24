package com.civicpilot.dto;

public class AiVisualGenerationResponse {

    private Long visualId;
    private Long journeyId;
    private Long serviceId;
    private String title;
    private String description;
    private String imageUrl;
    private String type;

    // No-argument constructor
    public AiVisualGenerationResponse() {
    }

    // Parameterized constructor
    public AiVisualGenerationResponse(
            Long visualId,
            Long journeyId,
            Long serviceId,
            String title,
            String description,
            String imageUrl,
            String type) {

        this.visualId = visualId;
        this.journeyId = journeyId;
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    // Getters

    public Long getVisualId() {
        return visualId;
    }

    public Long getJourneyId() {
        return journeyId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    // Setters

    public void setVisualId(Long visualId) {
        this.visualId = visualId;
    }

    public void setJourneyId(Long journeyId) {
        this.journeyId = journeyId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setType(String type) {
        this.type = type;
    }
}