package com.devops.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevOps Demo API")
                        .version(appVersion)
                        .description("A production-ready Spring Boot application for learning DevOps concepts. " +
                                "Features include REST APIs, WebSocket real-time updates, health checks, " +
                                "Prometheus metrics, and Kubernetes-ready configurations.")
                        .contact(new Contact()
                                .name("DevOps Team")
                                .email("devops@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("/").description("Current Server")
                ));
    }
}
