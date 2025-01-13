package com.example.canvasintegration.service;

import com.example.canvasintegration.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
        logger.debug("Authorization Header: Bearer {}", accessToken);
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
            logger.error("Failed to fetch courses: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch courses: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getQuizSubmissions(String courseId, String quizId) {
        try {
            String url = canvasApiUrl + "/courses/" + courseId + "/quizzes/" + quizId + "/submissions";
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
            logger.error("Failed to fetch quiz submissions for course {} and quiz {}: {}", courseId, quizId, e.getMessage());
            throw new RuntimeException("Failed to fetch quiz submissions: " + e.getMessage());
        }
    }

    public List<Student> getAllStudentsWithRanks(String courseId) {
        List<Student> allStudents = new ArrayList<>();

        logger.debug("Fetching students for course: {}", courseId);
        List<Student> courseStudents = getStudentsFromCourse(courseId);
        for (Student student : courseStudents) {
            if (student.getId() != null) {
                logger.debug("Fetching grades for student: {} in course: {}", student.getId(), courseId);
                Map<String, Object> grades = getStudentGrades(courseId, student.getId().toString());
                student.setGrades(grades);
                allStudents.add(student);
            }
        }

        return allStudents;
    }

    private List<Student> getStudentsFromCourse(String courseId) {
        try {
            String url = canvasApiUrl + "/courses/" + courseId + "/users?enrollment_type[]=student";
            logger.debug("Making request to: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());

            ResponseEntity<List<Student>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Student>>() {}
            );

            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Failed to fetch students from Canvas for course {}: {}", courseId, e.getMessage());
            throw new RuntimeException("Failed to fetch students from Canvas: " + e.getMessage());
        }
    }

    private Map<String, Object> getStudentGrades(String courseId, String studentId) {
        try {
            String url = canvasApiUrl + "/courses/" + courseId + "/students/submissions?student_ids[]=" + studentId;
            logger.debug("Making request to: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getBody() != null && !response.getBody().isEmpty()) {
                return response.getBody().get(0);
            }
            return new HashMap<>();
        } catch (Exception e) {
            logger.error("Failed to fetch grades for student {} in course {}: {}", studentId, courseId, e.getMessage());
            throw new RuntimeException("Failed to fetch grades: " + e.getMessage());
        }
    }

    public Student getStudentDetailedReport(String studentId, String courseId) {
        List<Student> students = getStudentsFromCourse(courseId);
        for (Student student : students) {
            if (student.getId().toString().equals(studentId)) {
                Map<String, Object> grades = getStudentGrades(courseId, studentId);
                student.setGrades(grades);
                return student;
            }
        }
        return null;
    }

    public List<Map<String, Object>> getStudentCourses(String studentId) {
        try {
            String url = canvasApiUrl + "/students/" + studentId + "/courses";
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
            logger.error("Failed to fetch courses for student {}: {}", studentId, e.getMessage());
            throw new RuntimeException("Failed to fetch student courses: " + e.getMessage());
        }
    }
}
