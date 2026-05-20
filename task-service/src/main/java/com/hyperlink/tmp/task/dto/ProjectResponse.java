package com.hyperlink.tmp.task.dto;

import com.hyperlink.tmp.task.model.Project;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProjectResponse {

    private UUID id;
    private String name;
    private String description;
    private UUID ownerUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProjectResponse from(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.id = project.getId();
        response.name = project.getName();
        response.description = project.getDescription();
        response.ownerUserId = project.getOwnerUserId();
        response.createdAt = project.getCreatedAt();
        response.updatedAt = project.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getOwnerUserId() {
        return ownerUserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
