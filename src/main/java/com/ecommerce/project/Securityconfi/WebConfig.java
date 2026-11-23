package com.ecommerce.project.Securityconfi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.url:}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = (frontendUrl == null || frontendUrl.isBlank())
                ? new String[]{"http://localhost:5173", "http://localhost:3000"}
                : new String[]{"http://localhost:5173", "http://localhost:3000", frontendUrl};

        registry.addMapping("/**")
                .allowedOrigins(Arrays.stream(origins).distinct().toArray(String[]::new))
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}