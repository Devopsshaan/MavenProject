package com.devops.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMetrics {
    private Instant timestamp;
    private double cpuUsage;
    private long usedMemory;
    private long totalMemory;
    private long freeMemory;
    private double memoryUsagePercent;
    private int activeThreads;
    private long uptimeSeconds;
    private String hostname;
    private String javaVersion;
    private int availableProcessors;
    private long heapUsed;
    private long heapMax;
    private double heapUsagePercent;
}
