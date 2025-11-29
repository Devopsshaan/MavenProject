package com.devops.demo.service;

import com.devops.demo.model.Task;
import com.devops.demo.model.Task.TaskStatus;
import com.devops.demo.repository.TaskRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final Counter tasksCreatedCounter;
    private final Counter tasksCompletedCounter;
    private final Timer taskOperationTimer;

    public TaskService(TaskRepository taskRepository, MeterRegistry meterRegistry) {
        this.taskRepository = taskRepository;

        // Custom metrics for DevOps monitoring
        this.tasksCreatedCounter = Counter.builder("tasks.created.total")
                .description("Total number of tasks created")
                .register(meterRegistry);

        this.tasksCompletedCounter = Counter.builder("tasks.completed.total")
                .description("Total number of tasks completed")
                .register(meterRegistry);

        this.taskOperationTimer = Timer.builder("tasks.operation.duration")
                .description("Time taken for task operations")
                .register(meterRegistry);
    }

    @Cacheable(value = "tasks")
    public List<Task> getAllTasks() {
        log.info("Fetching all tasks from database");
        return taskRepository.findAllOrderByCreatedAtDesc();
    }

    @Cacheable(value = "task", key = "#id")
    public Optional<Task> getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);
        return taskRepository.findById(id);
    }

    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public Task createTask(Task task) {
        return taskOperationTimer.record(() -> {
            log.info("Creating new task: {}", task.getTitle());
            Task savedTask = taskRepository.save(task);
            tasksCreatedCounter.increment();
            return savedTask;
        });
    }

    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public Optional<Task> updateTask(Long id, Task taskDetails) {
        return taskOperationTimer.record(() -> {
            log.info("Updating task with id: {}", id);
            return taskRepository.findById(id)
                    .map(existingTask -> {
                        existingTask.setTitle(taskDetails.getTitle());
                        existingTask.setDescription(taskDetails.getDescription());
                        existingTask.setStatus(taskDetails.getStatus());
                        existingTask.setPriority(taskDetails.getPriority());

                        if (taskDetails.getStatus() == TaskStatus.COMPLETED &&
                                existingTask.getStatus() != TaskStatus.COMPLETED) {
                            tasksCompletedCounter.increment();
                        }

                        return taskRepository.save(existingTask);
                    });
        });
    }

    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public boolean deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public TaskStatistics getStatistics() {
        long total = taskRepository.count();
        long pending = taskRepository.countByStatus(TaskStatus.PENDING);
        long inProgress = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long completed = taskRepository.countByStatus(TaskStatus.COMPLETED);
        long cancelled = taskRepository.countByStatus(TaskStatus.CANCELLED);

        return new TaskStatistics(total, pending, inProgress, completed, cancelled);
    }

    public record TaskStatistics(
            long total,
            long pending,
            long inProgress,
            long completed,
            long cancelled
    ) {}
}
