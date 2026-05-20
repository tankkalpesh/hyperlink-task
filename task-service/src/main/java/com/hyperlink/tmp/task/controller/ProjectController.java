package com.hyperlink.tmp.task.controller;

import com.hyperlink.tmp.task.dto.ProjectRequest;
import com.hyperlink.tmp.task.dto.ProjectResponse;
import com.hyperlink.tmp.task.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@PreAuthorize("isAuthenticated()")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request, Authentication authentication) {
        UUID ownerUserId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(projectService.create(request, ownerUserId));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listAll() {
        return ResponseEntity.ok(projectService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> update(@PathVariable("id") UUID id, @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
