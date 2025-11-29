package com.devops.demo;

import com.devops.demo.model.Task;
import com.devops.demo.repository.TaskRepository;
import com.devops.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Disabled;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DevOpsDemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(taskService).isNotNull();
    }

    // ==================== Health Check Tests ====================

    @Test
    @DisplayName("Actuator health endpoint returns UP")
    void healthEndpointReturnsUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    @DisplayName("Liveness probe returns UP")
    void livenessProbeReturnsUp() throws Exception {
        mockMvc.perform(get("/api/v1/health/live"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    @DisplayName("Readiness probe returns UP")
    void readinessProbeReturnsUp() throws Exception {
        mockMvc.perform(get("/api/v1/health/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    @Disabled("Prometheus endpoint not exposed in test context")
    @DisplayName("Prometheus metrics endpoint is accessible")
    void prometheusMetricsEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("jvm_memory")));
    }

    // ==================== Task API Tests ====================

    @Test
    @DisplayName("Create a new task")
    void createTask() throws Exception {
        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .priority(Task.TaskPriority.HIGH)
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Test Task")))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.priority", is("HIGH")));
    }

    @Test
    @DisplayName("Get all tasks")
    void getAllTasks() throws Exception {
        // Create test tasks
        taskService.createTask(Task.builder().title("Task 1").build());
        taskService.createTask(Task.builder().title("Task 2").build());

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Get task by ID")
    void getTaskById() throws Exception {
        Task created = taskService.createTask(Task.builder()
                .title("Find Me")
                .description("A task to find")
                .build());

        mockMvc.perform(get("/api/v1/tasks/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Find Me")));
    }

    @Test
    @DisplayName("Get task by ID - Not Found")
    void getTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update a task")
    void updateTask() throws Exception {
        Task created = taskService.createTask(Task.builder()
                .title("Original Title")
                .build());

        Task updated = Task.builder()
                .title("Updated Title")
                .status(Task.TaskStatus.IN_PROGRESS)
                .priority(Task.TaskPriority.CRITICAL)
                .build();

        mockMvc.perform(put("/api/v1/tasks/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.priority", is("CRITICAL")));
    }

    @Test
    @DisplayName("Delete a task")
    void deleteTask() throws Exception {
        Task created = taskService.createTask(Task.builder()
                .title("Delete Me")
                .build());

        mockMvc.perform(delete("/api/v1/tasks/" + created.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/v1/tasks/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get tasks by status")
    void getTasksByStatus() throws Exception {
        taskService.createTask(Task.builder().title("Pending Task").status(Task.TaskStatus.PENDING).build());
        taskService.createTask(Task.builder().title("Completed Task").status(Task.TaskStatus.COMPLETED).build());

        mockMvc.perform(get("/api/v1/tasks/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Pending Task")));
    }

    @Test
    @DisplayName("Get task statistics")
    void getTaskStatistics() throws Exception {
        taskService.createTask(Task.builder().title("Task 1").status(Task.TaskStatus.PENDING).build());
        taskService.createTask(Task.builder().title("Task 2").status(Task.TaskStatus.COMPLETED).build());
        taskService.createTask(Task.builder().title("Task 3").status(Task.TaskStatus.IN_PROGRESS).build());

        mockMvc.perform(get("/api/v1/tasks/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(3)))
                .andExpect(jsonPath("$.pending", is(1)))
                .andExpect(jsonPath("$.completed", is(1)))
                .andExpect(jsonPath("$.inProgress", is(1)));
    }

    // ==================== Validation Tests ====================

    @Test
    @DisplayName("Create task with invalid data - empty title")
    void createTaskInvalidTitle() throws Exception {
        Task task = Task.builder()
                .title("")
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest());
    }

    // ==================== System Endpoint Tests ====================

    @Test
    @DisplayName("Get application info")
    void getApplicationInfo() throws Exception {
        mockMvc.perform(get("/api/v1/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("devops-demo")))
                .andExpect(jsonPath("$.java", notNullValue()));
    }

    @Test
    @DisplayName("Get system metrics")
    void getSystemMetrics() throws Exception {
        mockMvc.perform(get("/api/v1/metrics/system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpuUsage", notNullValue()))
                .andExpect(jsonPath("$.usedMemory", notNullValue()))
                .andExpect(jsonPath("$.activeThreads", notNullValue()))
                .andExpect(jsonPath("$.uptimeSeconds", notNullValue()));
    }

    @Test
    @DisplayName("Simulate CPU load")
    void simulateLoad() throws Exception {
        mockMvc.perform(get("/api/v1/simulate/load"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Load simulation completed")))
                .andExpect(jsonPath("$.durationMs", notNullValue()));
    }

    // ==================== API Documentation Tests ====================

    @Test
    @DisplayName("Swagger UI is accessible")
    void swaggerUiIsAccessible() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("OpenAPI docs are accessible")
    void openApiDocsAccessible() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi", notNullValue()));
    }
}
