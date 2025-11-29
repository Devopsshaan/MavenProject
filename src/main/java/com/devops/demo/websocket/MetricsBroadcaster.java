package com.devops.demo.websocket;

import com.devops.demo.model.SystemMetrics;
import com.devops.demo.service.MetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;
    private final MetricsService metricsService;

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
