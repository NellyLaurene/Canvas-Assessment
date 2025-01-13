package com.example.canvasintegration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class CanvasConfig {

    @Value("${canvas.api.token}")
    private String apiToken;

    @Value("${canvas.api.base-url}")
    private String apiBaseUrl;

    @Value("${canvas.course.math-id}")
    private String mathCourseId;

    @Value("${canvas.course.biology-id}")
    private String biologyCourseId;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getMathCourseId() {
        return mathCourseId;
    }

    public String getBiologyCourseId() {
        return biologyCourseId;
    }

    // Example of making a GET request using RestTemplate
    public String getCanvasData(String courseId) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl)
                .pathSegment("courses", courseId, "students")
                .toUriString();
        try {
            return restTemplate().getForObject(url, String.class);
        } catch (HttpClientErrorException e) {
            // Handle exception, e.g., logging
            return "Error fetching data: " + e.getMessage();
        }
    }
}
