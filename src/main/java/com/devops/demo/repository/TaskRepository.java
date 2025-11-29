package com.devops.demo.repository;

import com.devops.demo.model.Task;
import com.devops.demo.model.Task.TaskStatus;
import com.devops.demo.model.Task.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = ?1")
    long countByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t ORDER BY t.createdAt DESC")
    List<Task> findAllOrderByCreatedAtDesc();

    List<Task> findByTitleContainingIgnoreCase(String title);
}
