package com.example.canvasintegration.service;

import com.example.canvasintegration.model.Student;
import com.example.canvasintegration.model.QuizSubmission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    public List<Student> getAllStudents(String courseId) {
        String url = canvasApiUrl + "/courses/" + courseId + "/users?enrollment_type[]=student";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> responseBody = response.getBody();
            List<Student> students = new ArrayList<>();
            if (responseBody != null) {
                for (Map<String, Object> item : responseBody) {
                    Student student = new Student();
                    student.setId(item.get("id").toString());
                    student.setName(item.get("name").toString());
                    student.setEmail(item.get("email") != null ? item.get("email").toString() : "No email");
                    students.add(student);
                }
            }
            return students;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch students: " + e.getMessage(), e);
        }
    }

    public Student getStudentById(String courseId, String studentId) {
        String url = canvasApiUrl + "/courses/" + courseId + "/users/" + studentId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null) {
                Student student = new Student();
                student.setId(responseBody.get("id").toString());
                student.setName(responseBody.get("name").toString());
                student.setEmail(responseBody.get("email") != null ? responseBody.get("email").toString() : "No email");
                return student;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch student: " + e.getMessage(), e);
        }
    }

    public List<QuizSubmission> getQuizSubmissions(String courseId, String quizId) {
        String url = canvasApiUrl + "/courses/" + courseId + "/quizzes/" + quizId + "/submissions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> responseBody = response.getBody();
            List<QuizSubmission> submissions = new ArrayList<>();
            if (responseBody != null) {
                for (Map<String, Object> item : responseBody) {
                    QuizSubmission submission = new QuizSubmission();
                    submission.setId(item.get("id").toString());
                    submission.setQuizId(item.get("quiz_id").toString());
                    submission.setUserId(item.get("user_id").toString());
                    submission.setScore(item.get("score") != null ? Double.valueOf(item.get("score").toString()) : 0.0);
                    submission.setMaxScore(item.get("quiz_points_possible") != null ? Double.valueOf(item.get("quiz_points_possible").toString()) : 0.0);
                    submissions.add(submission);
                }
            }
            return submissions;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch quiz submissions: " + e.getMessage(), e);
        }
    }
}
