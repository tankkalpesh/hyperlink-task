package com.hyperlink.tmp.task.controller;

import com.hyperlink.tmp.task.dto.AssignRequest;
import com.hyperlink.tmp.task.dto.TaskRequest;
import com.hyperlink.tmp.task.dto.TaskResponse;
import com.hyperlink.tmp.task.dto.UpdateStatusRequest;
import com.hyperlink.tmp.task.service.TaskService;
import com.hyperlink.tmp.task.util.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@PreAuthorize("isAuthenticated()")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@PathVariable("projectId") UUID projectId,
                                               @Valid @RequestBody TaskRequest request,
                                               Authentication authentication) {
        UUID ownerUserId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(taskService.create(projectId, request, ownerUserId));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> list(@PathVariable("projectId") UUID projectId,
                                                   @RequestParam(value = "status", required = false) Status status) {
        return ResponseEntity.ok(taskService.listByProject(projectId, status));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getById(@PathVariable("projectId") UUID projectId,
                                               @PathVariable("taskId") UUID taskId) {
        return ResponseEntity.ok(taskService.getById(projectId, taskId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> update(@PathVariable("projectId") UUID projectId,
                                              @PathVariable("taskId") UUID taskId,
                                              @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.update(projectId, taskId, request));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("projectId") UUID projectId,
                                       @PathVariable("taskId") UUID taskId) {
        taskService.delete(projectId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> assign(@PathVariable("projectId") UUID projectId,
                                               @PathVariable("taskId") UUID taskId,
                                               @Valid @RequestBody AssignRequest request,
                                               HttpServletRequest servletRequest) {
        return ResponseEntity.ok(taskService.assign(projectId, taskId, request.getAssigneeUserId()));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable("projectId") UUID projectId,
                                                   @PathVariable("taskId") UUID taskId,
                                                   @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(projectId, taskId, request.getStatus()));
    }

}
