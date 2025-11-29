package com.devops.demo.service;

import com.devops.demo.model.SystemMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.time.Instant;

@Service
public class MetricsService {

    private static final Logger log = LoggerFactory.getLogger(MetricsService.class);

    private final MemoryMXBean memoryMXBean;
    private final RuntimeMXBean runtimeMXBean;
    private final com.sun.management.OperatingSystemMXBean osMXBean;

    public MetricsService() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        this.osMXBean = (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
    }

    public SystemMetrics collectMetrics() {
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double memoryUsagePercent = ((double) usedMemory / totalMemory) * 100;

        long heapUsed = memoryMXBean.getHeapMemoryUsage().getUsed();
        long heapMax = memoryMXBean.getHeapMemoryUsage().getMax();
        double heapUsagePercent = heapMax > 0 ? ((double) heapUsed / heapMax) * 100 : 0;

        double cpuUsage = osMXBean.getCpuLoad() * 100;
        if (cpuUsage < 0) cpuUsage = 0;

        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            hostname = "unknown";
        }

        return SystemMetrics.builder()
                .timestamp(Instant.now())
                .cpuUsage(Math.round(cpuUsage * 100.0) / 100.0)
                .usedMemory(usedMemory / (1024 * 1024))
                .totalMemory(totalMemory / (1024 * 1024))
                .freeMemory(freeMemory / (1024 * 1024))
                .memoryUsagePercent(Math.round(memoryUsagePercent * 100.0) / 100.0)
                .activeThreads(Thread.activeCount())
                .uptimeSeconds(runtimeMXBean.getUptime() / 1000)
                .hostname(hostname)
                .javaVersion(System.getProperty("java.version"))
                .availableProcessors(runtime.availableProcessors())
                .heapUsed(heapUsed / (1024 * 1024))
                .heapMax(heapMax / (1024 * 1024))
                .heapUsagePercent(Math.round(heapUsagePercent * 100.0) / 100.0)
                .build();
    }
}
