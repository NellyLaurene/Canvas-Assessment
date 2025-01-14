package com.example.canvasintegration.service;

import com.example.canvasintegration.exception.ResourceNotFoundException;
import com.example.canvasintegration.model.Student;
import com.example.canvasintegration.model.Quiz;
import com.example.canvasintegration.model.QuizSubmission;
import com.example.canvasintegration.model.QuizSubmissionsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    // Fetch all students in a course
    public List<Student> getStudentsByCourseId(String courseId) {
        String url = canvasApiUrl + "/courses/" + courseId + "/users?enrollment_type[]=student&access_token=" + accessToken;
        return restTemplate.getForObject(url, List.class);
    }

    // Fetch student details for a specific course
    public Student getStudentDetails(String courseId, String studentId) {
        String url = canvasApiUrl + "/courses/" + courseId + "/users/" + studentId + "?access_token=" + accessToken;
        Student student = restTemplate.getForObject(url, Student.class);

        // Fetch submissions for the student
        String submissionsUrl = canvasApiUrl + "/courses/" + courseId + "/students/submissions?student_ids[]=" + studentId + "&access_token=" + accessToken;
        List<Student.Course> courses = restTemplate.getForObject(submissionsUrl, List.class);

        if (student != null) {
            student.setCourses(courses);
        }
        return student;
    }

    // Fetch quiz submissions for a specific quiz
    public List<QuizSubmission> getQuizSubmissions(String courseId, String quizId) {
        String url = canvasApiUrl + "/courses/" + courseId + "/quizzes/" + quizId + "/submissions?access_token=" + accessToken;

        QuizSubmissionsResponse response = restTemplate.getForObject(url, QuizSubmissionsResponse.class);
        if (response != null) {
            return response.getQuizSubmissions();
        }
        return List.of(); // Return an empty list if response is null
    }

    // Fetch all quizzes in a course
    public List<Quiz> getQuizzesByCourseId(String courseId) {
        String url = canvasApiUrl + "/courses/" + courseId + "/quizzes?access_token=" + accessToken;
        try {
            return restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Quiz>>() {}).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Quiz not found for the course: " + courseId);
        }
    }

    // Fetch all courses
    public List<Student.Course> getAllCourses() {
        String url = canvasApiUrl + "/courses?access_token=" + accessToken;
        return restTemplate.getForObject(url, List.class);
    }
}