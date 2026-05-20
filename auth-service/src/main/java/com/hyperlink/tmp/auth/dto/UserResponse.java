package com.hyperlink.tmp.auth.dto;

import com.hyperlink.tmp.auth.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private String role;
    private LocalDateTime createdAt;
    private boolean isActive;

    public static UserResponse from(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.id = user.getId();
        userResponse.email = user.getEmail();
        userResponse.fullName = user.getFullName();
        userResponse.role = user.getRole() != null ? user.getRole().name() : null;
        userResponse.createdAt = user.getCreatedAt();
        userResponse.isActive = user.isActive();
        return userResponse;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }
}
