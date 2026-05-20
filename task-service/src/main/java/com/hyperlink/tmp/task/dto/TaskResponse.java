package com.hyperlink.tmp.task.dto;

import com.hyperlink.tmp.task.model.Task;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private UUID projectId;
    private UUID assigneeUserId;
    private String priority;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskResponse from(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.id = task.getId();
        taskResponse.title = task.getTitle();
        taskResponse.description = task.getDescription();
        taskResponse.projectId = task.getProjectId();
        taskResponse.assigneeUserId = task.getAssigneeUserId();
        taskResponse.priority = task.getPriority() != null ? task.getPriority().name() : null;
        taskResponse.status = task.getStatus() != null ? task.getStatus().name() : null;
        taskResponse.createdAt = task.getCreatedAt();
        taskResponse.updatedAt = task.getUpdatedAt();
        return taskResponse;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public UUID getAssigneeUserId() {
        return assigneeUserId;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
