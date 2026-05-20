package com.hyperlink.tmp.task.repository;

import com.hyperlink.tmp.task.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
