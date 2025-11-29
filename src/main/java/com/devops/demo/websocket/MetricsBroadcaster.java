package com.devops.demo.websocket;

import com.devops.demo.model.SystemMetrics;
import com.devops.demo.service.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetricsBroadcaster {

    private static final Logger log = LoggerFactory.getLogger(MetricsBroadcaster.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final MetricsService metricsService;

    public MetricsBroadcaster(SimpMessagingTemplate messagingTemplate, MetricsService metricsService) {
        this.messagingTemplate = messagingTemplate;
        this.metricsService = metricsService;
    }

    @Scheduled(fixedRate = 2000) // Broadcast every 2 seconds
    public void broadcastMetrics() {
        try {
            SystemMetrics metrics = metricsService.collectMetrics();
            messagingTemplate.convertAndSend("/topic/metrics", metrics);
            log.debug("Broadcasted metrics: CPU={}%, Memory={}%",
                    metrics.getCpuUsage(), metrics.getMemoryUsagePercent());
        } catch (Exception e) {
            log.error("Error broadcasting metrics", e);
        }
    }
}
