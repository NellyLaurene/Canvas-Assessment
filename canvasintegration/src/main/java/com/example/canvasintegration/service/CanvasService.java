package com.example.canvasintegration.service;

import com.example.canvasintegration.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CanvasService {

    @Value("${canvas.api.base-url}")
    private String canvasApiUrl;

    @Value("${canvas.api.token}")
    private String accessToken;

    private final RestTemplate restTemplate;

    public CanvasService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Student> getAllStudents() {
        String url = canvasApiUrl + "/students"; // Adjust based on Canvas API structure
        return fetchFromCanvasAPI(url, List.class);
    }

    public Student getStudentById(String id) {
        String url = canvasApiUrl + "/students/" + id; // Adjust based on Canvas API structure
        return fetchFromCanvasAPI(url, Student.class);
    }

    private <T> T fetchFromCanvasAPI(String url, Class<T> responseType) {
        return restTemplate.getForObject(url + "?access_token=" + accessToken, responseType);
    }
}
