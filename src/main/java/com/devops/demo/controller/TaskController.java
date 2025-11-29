package com.devops.demo.controller;

import com.devops.demo.model.Task;
import com.devops.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task Management", description = "APIs for managing tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve all tasks ordered by creation date")
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("GET /api/v1/tasks - Fetching all tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        log.info("GET /api/v1/tasks/{} - Fetching task", id);
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task with the provided details")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        log.info("POST /api/v1/tasks - Creating task: {}", task.getTitle());
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Update an existing task")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        log.info("PUT /api/v1/tasks/{} - Updating task", id);
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("DELETE /api/v1/tasks/{} - Deleting task", id);
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tasks by status", description = "Filter tasks by their status")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Task.TaskStatus status) {
        log.info("GET /api/v1/tasks/status/{} - Fetching tasks by status", status);
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get task statistics", description = "Get aggregated task statistics")
    public ResponseEntity<TaskService.TaskStatistics> getStatistics() {
        log.info("GET /api/v1/tasks/statistics - Fetching statistics");
        return ResponseEntity.ok(taskService.getStatistics());
    }
}
