package com.hyperlink.tmp.task.service;

import com.hyperlink.tmp.task.dto.TaskRequest;
import com.hyperlink.tmp.task.dto.TaskResponse;
import com.hyperlink.tmp.task.model.Project;
import com.hyperlink.tmp.task.model.Task;
import com.hyperlink.tmp.task.repository.ProjectRepository;
import com.hyperlink.tmp.task.repository.TaskRepository;
import com.hyperlink.tmp.task.util.Status;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final RestTemplate restTemplate;
    private final String authBaseUrl;

    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository,
                           RestTemplate restTemplate,
                           @Value("${auth.base.url}") String authBaseUrl) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.restTemplate = restTemplate;
        this.authBaseUrl = authBaseUrl;
    }

    @Override
    public TaskResponse create(UUID projectId, TaskRequest request, UUID ownerUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new java.util.NoSuchElementException("Project not found"));
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setProjectId(project.getId());
        task.setAssigneeUserId(request.getAssigneeUserId());
        return TaskResponse.from(taskRepository.save(task));
    }

    @Override
    public List<TaskResponse> listByProject(UUID projectId) {
        return taskRepository.findByProjectId(projectId).stream().map(TaskResponse::from).toList();
    }

    @Override
    public TaskResponse getById(UUID projectId, UUID taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new java.util.NoSuchElementException("Task not found"));
        if (!task.getProjectId().equals(projectId)) throw new java.util.NoSuchElementException("Task not found in project");
        return TaskResponse.from(task);
    }

    @Override
    public TaskResponse update(UUID projectId, UUID taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new java.util.NoSuchElementException("Task not found"));
        if (!task.getProjectId().equals(projectId)) throw new java.util.NoSuchElementException("Task not found in project");
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setUpdatedAt(LocalDateTime.now());
        return TaskResponse.from(taskRepository.save(task));
    }

    @Override
    public void delete(UUID projectId, UUID taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new java.util.NoSuchElementException("Task not found"));
        if (!task.getProjectId().equals(projectId)) throw new java.util.NoSuchElementException("Task not found in project");
        taskRepository.deleteById(taskId);
    }

    @Override
    public TaskResponse assign(UUID projectId, UUID taskId, UUID assigneeUserId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new java.util.NoSuchElementException("Task not found"));
        if (!task.getProjectId().equals(projectId)) throw new java.util.NoSuchElementException("Task not found in project");
        String url = authBaseUrl + "/api/v1/users/" + assigneeUserId;
        try {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            HttpEntity<Void> entity = null;
            if (attrs instanceof ServletRequestAttributes) {
                HttpServletRequest current = ((ServletRequestAttributes) attrs).getRequest();
                String authHeader = current.getHeader("Authorization");
                HttpHeaders headers = new HttpHeaders();
                if (authHeader != null) headers.set("Authorization", authHeader);
                entity = new HttpEntity<>(headers);
            }
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
                ObjectMapper om = new ObjectMapper();
                JsonNode node = om.readTree(resp.getBody());
                boolean isActive = node.path("active").asBoolean(false);
                if (!isActive) throw new IllegalArgumentException("Assignee user is not active");
                task.setAssigneeUserId(assigneeUserId);
                task.setUpdatedAt(LocalDateTime.now());
                return TaskResponse.from(taskRepository.save(task));
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Assignee user not found or inaccessible");
        }
        throw new IllegalArgumentException("Assignee user not found");
    }

    @Override
    public TaskResponse updateStatus(UUID projectId, UUID taskId, Status status) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new java.util.NoSuchElementException("Task not found"));
        if (!task.getProjectId().equals(projectId)) throw new java.util.NoSuchElementException("Task not found in project");
        Status current = task.getStatus();
        boolean allowed = false;
        if (current == null) {
            allowed = status == Status.TODO;
        } else if (current == Status.TODO) {
            allowed = status == Status.IN_PROGRESS;
        } else if (current == Status.IN_PROGRESS) {
            allowed = status == Status.DONE;
        } else if (current == Status.DONE) {
            allowed = status == Status.IN_PROGRESS;
        }

        if (!allowed) {
            throw new IllegalArgumentException("Invalid status transition from " + current + " to " + status);
        }
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        return TaskResponse.from(taskRepository.save(task));
    }

    @Override
    public List<TaskResponse> myTasks(UUID userId) {
        return taskRepository.findByAssigneeUserId(userId).stream().map(TaskResponse::from).toList();
    }
}
