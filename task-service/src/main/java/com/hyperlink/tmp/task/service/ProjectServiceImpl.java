package com.hyperlink.tmp.task.service;

import com.hyperlink.tmp.task.dto.ProjectRequest;
import com.hyperlink.tmp.task.dto.ProjectResponse;
import com.hyperlink.tmp.task.model.Project;
import com.hyperlink.tmp.task.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectResponse create(ProjectRequest request, UUID ownerUserId) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwnerUserId(ownerUserId);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Override
    public List<ProjectResponse> listAll() {
        return projectRepository.findAll().stream().map(ProjectResponse::from).toList();
    }

    @Override
    public ProjectResponse getById(UUID id) {
        return projectRepository.findById(id)
                .map(ProjectResponse::from)
                .orElseThrow(() -> new java.util.NoSuchElementException("Project not found"));
    }

    @Override
    public ProjectResponse update(UUID id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Project not found"));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setUpdatedAt(LocalDateTime.now());
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Override
    public void delete(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new java.util.NoSuchElementException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
