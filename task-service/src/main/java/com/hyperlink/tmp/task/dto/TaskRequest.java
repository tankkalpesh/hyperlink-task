package com.hyperlink.tmp.task.dto;

import com.hyperlink.tmp.task.util.Priority;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class TaskRequest {

    @NotBlank
    private String title;

    private String description;

    private Priority priority;

    private UUID assigneeUserId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public UUID getAssigneeUserId() { return assigneeUserId; }
    public void setAssigneeUserId(UUID assigneeUserId) { this.assigneeUserId = assigneeUserId; }
}
