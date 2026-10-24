package com.civicpilot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "visual_resources")
public class VisualResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = true)
    private GovernmentService governmentService;

    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = true)
    private CitizenJourney citizenJourney;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisualType type;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "image_prompt", columnDefinition = "TEXT")
    private String imagePrompt;

    @Column(name = "official_link", columnDefinition = "TEXT")
    private String officialLink;

    @Column(nullable = false)
    private String language;

    private LocalDateTime createdAt;

    public VisualResource() {
    }

    public Long getId() {
        return id;
    }

    public GovernmentService getGovernmentService() {
        return governmentService;
    }

    public CitizenJourney getCitizenJourney() {
        return citizenJourney;
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

    public String getImagePrompt() {
        return imagePrompt;
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

    public void setGovernmentService(GovernmentService governmentService) {
        this.governmentService = governmentService;
    }

    public void setCitizenJourney(CitizenJourney citizenJourney) {
        this.citizenJourney = citizenJourney;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(VisualType type) {
        this.type = type;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImagePrompt(String imagePrompt) {
        this.imagePrompt = imagePrompt;
    }

    public void setOfficialLink(String officialLink) {
        this.officialLink = officialLink;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}