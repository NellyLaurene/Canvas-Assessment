package com.example.canvasintegration.service;

import com.example.canvasintegration.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CanvasService {

    private static final Logger logger = LoggerFactory.getLogger(CanvasService.class);

    private final RestTemplate restTemplate;

    @Value("${canvas.api.base-url}")
    private String canvasApiUrl;

    @Value("${canvas.api.token}")
    private String accessToken;

    public CanvasService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<Map<String, Object>> getCourses() {
        try {
            String url = canvasApiUrl + "/courses";
            logger.debug("Making request to: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Failed to fetch courses: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch courses: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getQuizSubmissions(String courseId, String quizId) {
        try {
            String url = canvasApiUrl + "/courses/" + courseId + "/quizzes/" + quizId + "/submissions";
            logger.debug("Making request to: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getBody() != null && response.getBody().containsKey("quiz_submissions")) {
                return (List<Map<String, Object>>) response.getBody().get("quiz_submissions");
            }
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Failed to fetch quiz submissions for course {} and quiz {}: {}", courseId, quizId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch quiz submissions: " + e.getMessage(), e);
        }
    }
}
