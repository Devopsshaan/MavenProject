package com.devops.demo.controller;

import com.devops.demo.model.SystemMetrics;
import com.devops.demo.service.MetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "System Information", description = "APIs for system health and information")
public class SystemController {

    private final MetricsService metricsService;

    @Value("${spring.application.name:devops-demo}")
    private String applicationName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.environment:development}")
    private String environment;

    public SystemController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/info")
    @Operation(summary = "Get application info", description = "Returns application metadata")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", applicationName);
        info.put("version", appVersion);
        info.put("environment", environment);
        info.put("timestamp", Instant.now().toString());
        info.put("java", System.getProperty("java.version"));
        info.put("os", System.getProperty("os.name") + " " + System.getProperty("os.version"));
        return ResponseEntity.ok(info);
    }

    @GetMapping("/health/live")
    @Operation(summary = "Liveness probe", description = "Kubernetes liveness probe endpoint")
    public ResponseEntity<Map<String, String>> liveness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health/ready")
    @Operation(summary = "Readiness probe", description = "Kubernetes readiness probe endpoint")
    public ResponseEntity<Map<String, String>> readiness() {
        // Add actual readiness checks here (database, external services, etc.)
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/metrics/system")
    @Operation(summary = "Get system metrics", description = "Returns current system metrics")
    public ResponseEntity<SystemMetrics> getSystemMetrics() {
        return ResponseEntity.ok(metricsService.collectMetrics());
    }

    @GetMapping("/simulate/load")
    @Operation(summary = "Simulate CPU load", description = "Creates artificial CPU load for testing autoscaling")
    public ResponseEntity<Map<String, Object>> simulateLoad() {
        // Simulate some CPU work
        long start = System.currentTimeMillis();
        double result = 0;
        for (int i = 0; i < 10_000_000; i++) {
            result += Math.sqrt(i) * Math.sin(i);
        }
        long duration = System.currentTimeMillis() - start;

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Load simulation completed");
        response.put("durationMs", duration);
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/simulate/memory")
    @Operation(summary = "Simulate memory usage", description = "Creates temporary memory allocation for testing")
    public ResponseEntity<Map<String, Object>> simulateMemory() {
        // Allocate some memory temporarily
        byte[][] arrays = new byte[100][];
        for (int i = 0; i < 100; i++) {
            arrays[i] = new byte[1024 * 1024]; // 1MB each
        }

        SystemMetrics metrics = metricsService.collectMetrics();

        // Clear the arrays
        for (int i = 0; i < 100; i++) {
            arrays[i] = null;
        }
        System.gc();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Memory simulation completed");
        response.put("peakMemoryUsedMB", metrics.getUsedMemory());
        response.put("heapUsedMB", metrics.getHeapUsed());
        return ResponseEntity.ok(response);
    }
}
