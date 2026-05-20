package com.hyperlink.tmp.task.service;

import com.hyperlink.tmp.task.dto.AssignRequest;
import com.hyperlink.tmp.task.dto.TaskRequest;
import com.hyperlink.tmp.task.dto.TaskResponse;
import com.hyperlink.tmp.task.util.Status;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskResponse create(UUID projectId, TaskRequest request, UUID ownerUserId);
    List<TaskResponse> listByProject(UUID projectId);
    TaskResponse getById(UUID projectId, UUID taskId);
    TaskResponse update(UUID projectId, UUID taskId, TaskRequest request);
    void delete(UUID projectId, UUID taskId);
    TaskResponse assign(UUID projectId, UUID taskId, UUID assigneeUserId);
    TaskResponse updateStatus(UUID projectId, UUID taskId, Status status);
    List<TaskResponse> myTasks(UUID userId);
}
