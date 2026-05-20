package com.hyperlink.tmp.task.service;

import com.hyperlink.tmp.task.dto.ProjectRequest;
import com.hyperlink.tmp.task.dto.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request, UUID ownerUserId);
    List<ProjectResponse> listAll();
    ProjectResponse getById(UUID id);
    ProjectResponse update(UUID id, ProjectRequest request);
    void delete(UUID id);
}
