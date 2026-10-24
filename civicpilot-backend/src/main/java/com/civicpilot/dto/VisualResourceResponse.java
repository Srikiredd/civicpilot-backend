package com.civicpilot.dto;

import java.time.LocalDateTime;

import com.civicpilot.entity.VisualType;

public class VisualResourceResponse {

    private Long visualId;

    private Long serviceId;

    private String serviceName;

    private String title;

    private String description;

    private VisualType type;

    private String imageUrl;

    private String officialLink;

    private String language;

    private LocalDateTime createdAt;

    public VisualResourceResponse() {

    }

    public VisualResourceResponse(

            Long visualId,

            Long serviceId,

            String serviceName,

            String title,

            String description,

            VisualType type,

            String imageUrl,

            String officialLink,

            String language,

            LocalDateTime createdAt

    ) {

        this.visualId = visualId;

        this.serviceId = serviceId;

        this.serviceName = serviceName;

        this.title = title;

        this.description = description;

        this.type = type;

        this.imageUrl = imageUrl;

        this.officialLink = officialLink;

        this.language = language;

        this.createdAt = createdAt;

    }

    public Long getVisualId() {

        return visualId;

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

    public VisualType getType() {

        return type;

    }

    public String getImageUrl() {

        return imageUrl;

    }

    public String getOfficialLink() {

        return officialLink;

    }

    public String getLanguage() {

        return language;

    }

    public LocalDateTime getCreatedAt() {

        return createdAt;

    }

}