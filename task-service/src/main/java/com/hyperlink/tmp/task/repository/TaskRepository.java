package com.hyperlink.tmp.task.repository;

import com.hyperlink.tmp.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProjectId(UUID projectId);
    List<Task> findByAssigneeUserId(UUID assigneeUserId);
}
