package com.hyperlink.tmp.task.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AssignRequest {
    @NotNull(message = "assigneeUserId is required")
    private UUID assigneeUserId;

    public UUID getAssigneeUserId() { return assigneeUserId; }
    public void setAssigneeUserId(UUID assigneeUserId) { this.assigneeUserId = assigneeUserId; }
}
