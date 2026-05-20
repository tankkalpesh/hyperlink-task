package com.hyperlink.tmp.task.dto;

import com.hyperlink.tmp.task.util.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {
    @NotNull(message = "status is required")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
