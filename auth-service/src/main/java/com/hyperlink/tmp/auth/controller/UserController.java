package com.hyperlink.tmp.auth.controller;

import com.hyperlink.tmp.auth.dto.UserResponse;
import com.hyperlink.tmp.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new org.springframework.security.core.AuthenticationException("Unauthenticated") {};
        }
        String subject = auth.getName();
        UUID userId = UUID.fromString(subject);
        UserResponse resp = userService.getById(userId);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@org.springframework.web.bind.annotation.PathVariable("id") java.util.UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<java.util.List<UserResponse>> listAll() {
        return ResponseEntity.ok(userService.listAll());
    }
}
