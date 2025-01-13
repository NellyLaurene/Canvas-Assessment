package com.example.canvasintegration.service;

import com.example.canvasintegration.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CanvasService {
    private static final Logger logger = LoggerFactory.getLogger(CanvasService.class);

    @Value("${canvas.api.base-url}")
    private String canvasApiUrl;

    @Value("${canvas.api.token}")
    private String accessToken;

    @Value("${canvas.course.math-id}")
    private String mathCourseId;

    @Value("${canvas.course.biology-id}")
    private String biologyCourseId; // Added for multiple courses

    private final RestTemplate restTemplate;

    public CanvasService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    public List<Student> getAllStudents(String courseId) {
        try {
            String url = canvasApiUrl + "/courses/" + courseId + "/users?enrollment_type[]=student&include[]=enrollments";
            logger.debug("Fetching students from URL: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<List<Student>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Student>>() {}
            );

            List<Student> students = response.getBody() != null ? response.getBody() : new ArrayList<>();

            // Fetch grades for each student
            for (Student student : students) {
                Map<String, Object> grades = getStudentGrades(student.getId(), courseId);
                student.setGrades(grades);
            }

            return students;
        } catch (Exception e) {
            logger.error("Error fetching students: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch students from Canvas: " + e.getMessage());
        }
    }

    public List<Student> getAllStudentsWithRanks(String courseId) {
        List<Student> students = getAllStudents(courseId);
        calculateAndSetRanks(students);
        return students;
    }

    public Student getStudentDetailedReport(String studentId, String courseId) {
        try {
            // Get basic student information
            Student student = getStudentById(studentId, courseId);
            if (student == null) {
                throw new RuntimeException("Student not found");
            }

            // Get all enrollments for the student
            List<Map<String, Object>> enrollments = getStudentEnrollments(studentId);
            student.setEnrollments(Map.of("courses", enrollments));

            // Calculate and set student rank
            List<Student> allStudents = getAllStudents(courseId);
            calculateAndSetRanks(allStudents);
            Optional<Student> rankedStudent = allStudents.stream()
                    .filter(s -> s.getId().equals(studentId))
                    .findFirst();
            rankedStudent.ifPresent(s -> student.setRank(s.getRank()));

            return student;
        } catch (Exception e) {
            logger.error("Error generating student detailed report: {}", e.getMessage());
            throw new RuntimeException("Failed to generate student report: " + e.getMessage());
        }
    }

    private void calculateAndSetRanks(List<Student> students) {
        // Sort students by their average grade
        students.sort((s1, s2) -> {
            Double grade1 = calculateAverageGrade(s1.getGrades());
            Double grade2 = calculateAverageGrade(s2.getGrades());
            return grade2.compareTo(grade1); // Descending order
        });

        // Set ranks
        for (int i = 0; i < students.size(); i++) {
            students.get(i).setRank(i + 1);
        }
    }

    private Double calculateAverageGrade(Map<String, Object> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }

        try {
            // Extract grades from submissions
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> submissions = (List<Map<String, Object>>) grades.get("submissions");
            if (submissions == null || submissions.isEmpty()) {
                return 0.0;
            }

            // Calculate average of all valid grades
            double sum = 0;
            int count = 0;
            for (Map<String, Object> submission : submissions) {
                Object grade = submission.get("grade");
                if (grade != null) {
                    try {
                        sum += Double.parseDouble(grade.toString());
                        count++;
                    } catch (NumberFormatException ignored) {
                        // Skip non-numeric grades
                    }
                }
            }

            return count > 0 ? sum / count : 0.0;
        } catch (Exception e) {
            logger.error("Error calculating average grade: {}", e.getMessage());
            return 0.0;
        }
    }

    public Student getStudentById(String studentId, String courseId) {
        try {
            String url = canvasApiUrl + "/courses/" + courseId + "/users/" + studentId + "?include[]=enrollments";
            logger.debug("Fetching student details from URL: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<Student> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Student.class
            );

            Student student = response.getBody();
            if (student != null) {
                Map<String, Object> grades = getStudentGrades(studentId, courseId);
                student.setGrades(grades);
            }

            return student;
        } catch (Exception e) {
            logger.error("Error fetching student details: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch student details: " + e.getMessage());
        }
    }

    private Map<String, Object> getStudentGrades(String studentId, String courseId) {
        try {
            String url = canvasApiUrl + "/courses/" + courseId +
                    "/students/submissions?student_ids[]=" + studentId +
                    "&include[]=assignment&include[]=total_scores";
            logger.debug("Fetching student grades from URL: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            return response.getBody() != null ? response.getBody() : new HashMap<>();
        } catch (Exception e) {
            logger.error("Error fetching student grades: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    private List<Map<String, Object>> getStudentEnrollments(String studentId) {
        try {
            String url = canvasApiUrl + "/users/" + studentId + "/enrollments?include[]=total_scores";
            logger.debug("Fetching student enrollments from URL: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error fetching student enrollments: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public Map<String, Object> getCourseDetails(String courseId) {
        try {
            String url = canvasApiUrl + "/courses/" + courseId + "?include[]=total_students";
            logger.debug("Fetching course details from URL: {}", url);

            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            return response.getBody() != null ? response.getBody() : new HashMap<>();
        } catch (Exception e) {
            logger.error("Error fetching course details: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch course details: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getStudentCourses(String studentId) {
        return List.of();
    }
}
