package com.devops.demo.websocket;

import com.devops.demo.model.Task;
import com.devops.demo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Map;

@Controller
public class TaskWebSocketController {

    private static final Logger log = LoggerFactory.getLogger(TaskWebSocketController.class);

    private final TaskService taskService;
    private final SimpMessagingTemplate messagingTemplate;

    public TaskWebSocketController(TaskService taskService, SimpMessagingTemplate messagingTemplate) {
        this.taskService = taskService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/task/create")
    @SendTo("/topic/tasks")
    public Map<String, Object> createTask(Task task) {
        log.info("WebSocket: Creating task - {}", task.getTitle());
        Task created = taskService.createTask(task);
        return Map.of(
                "action", "CREATED",
                "task", created,
                "timestamp", Instant.now().toString()
        );
    }

    @MessageMapping("/task/update")
    @SendTo("/topic/tasks")
    public Map<String, Object> updateTask(Task task) {
        log.info("WebSocket: Updating task - {}", task.getId());
        return taskService.updateTask(task.getId(), task)
                .map(updated -> Map.<String, Object>of(
                        "action", "UPDATED",
                        "task", updated,
                        "timestamp", Instant.now().toString()
                ))
                .orElse(Map.of(
                        "action", "ERROR",
                        "message", "Task not found",
                        "timestamp", Instant.now().toString()
                ));
    }

    @MessageMapping("/ping")
    @SendTo("/topic/pong")
    public Map<String, Object> ping(Map<String, Object> message) {
        log.debug("WebSocket: Ping received");
        return Map.of(
                "message", "pong",
                "received", message,
                "timestamp", Instant.now().toString()
        );
    }

    // Method to broadcast task updates from REST endpoints
    public void broadcastTaskUpdate(String action, Task task) {
        messagingTemplate.convertAndSend("/topic/tasks", Map.of(
                "action", action,
                "task", task,
                "timestamp", Instant.now().toString()
        ));
    }
}
