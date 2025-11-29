package com.devops.demo.model;

import java.time.Instant;

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

    public SystemMetrics() {
    }

    // Builder pattern
    public static SystemMetricsBuilder builder() {
        return new SystemMetricsBuilder();
    }

    public static class SystemMetricsBuilder {
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

        public SystemMetricsBuilder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public SystemMetricsBuilder cpuUsage(double cpuUsage) {
            this.cpuUsage = cpuUsage;
            return this;
        }

        public SystemMetricsBuilder usedMemory(long usedMemory) {
            this.usedMemory = usedMemory;
            return this;
        }

        public SystemMetricsBuilder totalMemory(long totalMemory) {
            this.totalMemory = totalMemory;
            return this;
        }

        public SystemMetricsBuilder freeMemory(long freeMemory) {
            this.freeMemory = freeMemory;
            return this;
        }

        public SystemMetricsBuilder memoryUsagePercent(double memoryUsagePercent) {
            this.memoryUsagePercent = memoryUsagePercent;
            return this;
        }

        public SystemMetricsBuilder activeThreads(int activeThreads) {
            this.activeThreads = activeThreads;
            return this;
        }

        public SystemMetricsBuilder uptimeSeconds(long uptimeSeconds) {
            this.uptimeSeconds = uptimeSeconds;
            return this;
        }

        public SystemMetricsBuilder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public SystemMetricsBuilder javaVersion(String javaVersion) {
            this.javaVersion = javaVersion;
            return this;
        }

        public SystemMetricsBuilder availableProcessors(int availableProcessors) {
            this.availableProcessors = availableProcessors;
            return this;
        }

        public SystemMetricsBuilder heapUsed(long heapUsed) {
            this.heapUsed = heapUsed;
            return this;
        }

        public SystemMetricsBuilder heapMax(long heapMax) {
            this.heapMax = heapMax;
            return this;
        }

        public SystemMetricsBuilder heapUsagePercent(double heapUsagePercent) {
            this.heapUsagePercent = heapUsagePercent;
            return this;
        }

        public SystemMetrics build() {
            SystemMetrics metrics = new SystemMetrics();
            metrics.timestamp = this.timestamp;
            metrics.cpuUsage = this.cpuUsage;
            metrics.usedMemory = this.usedMemory;
            metrics.totalMemory = this.totalMemory;
            metrics.freeMemory = this.freeMemory;
            metrics.memoryUsagePercent = this.memoryUsagePercent;
            metrics.activeThreads = this.activeThreads;
            metrics.uptimeSeconds = this.uptimeSeconds;
            metrics.hostname = this.hostname;
            metrics.javaVersion = this.javaVersion;
            metrics.availableProcessors = this.availableProcessors;
            metrics.heapUsed = this.heapUsed;
            metrics.heapMax = this.heapMax;
            metrics.heapUsagePercent = this.heapUsagePercent;
            return metrics;
        }
    }

    // Getters and Setters
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public double getMemoryUsagePercent() {
        return memoryUsagePercent;
    }

    public void setMemoryUsagePercent(double memoryUsagePercent) {
        this.memoryUsagePercent = memoryUsagePercent;
    }

    public int getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(int activeThreads) {
        this.activeThreads = activeThreads;
    }

    public long getUptimeSeconds() {
        return uptimeSeconds;
    }

    public void setUptimeSeconds(long uptimeSeconds) {
        this.uptimeSeconds = uptimeSeconds;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public long getHeapUsed() {
        return heapUsed;
    }

    public void setHeapUsed(long heapUsed) {
        this.heapUsed = heapUsed;
    }

    public long getHeapMax() {
        return heapMax;
    }

    public void setHeapMax(long heapMax) {
        this.heapMax = heapMax;
    }

    public double getHeapUsagePercent() {
        return heapUsagePercent;
    }

    public void setHeapUsagePercent(double heapUsagePercent) {
        this.heapUsagePercent = heapUsagePercent;
    }
}
