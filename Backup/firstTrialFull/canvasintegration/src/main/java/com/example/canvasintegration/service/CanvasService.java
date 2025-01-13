package com.example.canvasintegration.service;

import com.example.canvasintegration.model.Course;
import com.example.canvasintegration.model.QuizSubmission;
import com.example.canvasintegration.model.Student;
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

    // Fetch published courses
    public List<Course> getPublishedCourses() {
        String url = canvasApiUrl + "/courses";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> responseBody = response.getBody();
            List<Course> courses = new ArrayList<>();
            if (responseBody != null) {
                for (Map<String, Object> item : responseBody) {
                    if ("available".equals(item.get("workflow_state"))) { // Filter only published courses
                        Course course = new Course();
                        course.setId(item.get("id").toString());
                        course.setName(item.get("name").toString());
                        course.setCourseCode(item.get("course_code").toString());
                        courses.add(course);
                    }
                }
            }
            return courses;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch courses: " + e.getMessage(), e);
        }
    }

    // Fetch all students for a specific course
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
                    student.setEmail(item.getOrDefault("email", "No email").toString());
                    students.add(student);
                }
            }
            return students;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch students for course " + courseId + ": " + e.getMessage(), e);
        }
    }

    // Fetch a single student by ID
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
                student.setEmail(responseBody.getOrDefault("email", "No email").toString());
                return student;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch student " + studentId + " for course " + courseId + ": " + e.getMessage(), e);
        }
    }

    // Fetch quiz submissions for a specific course and quiz
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
                    submission.setScore(parseDouble(item.get("score")));
                    submission.setMaxScore(parseDouble(item.get("quiz_points_possible")));
                    submissions.add(submission);
                }
            }
            return submissions;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch quiz submissions for quiz " + quizId + " in course " + courseId + ": " + e.getMessage(), e);
        }
    }

    // Helper method to safely parse doubles
    private Double parseDouble(Object value) {
        try {
            return value != null ? Double.valueOf(value.toString()) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
