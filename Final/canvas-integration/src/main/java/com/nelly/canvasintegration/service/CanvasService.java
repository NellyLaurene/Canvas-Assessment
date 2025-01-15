package com.nelly.canvasintegration.service;

import com.nelly.canvasintegration.model.Course;
import com.nelly.canvasintegration.model.CourseEnrollment;
import com.nelly.canvasintegration.model.CourseGrade;
import com.nelly.canvasintegration.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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

    private final RestTemplate restTemplate;

    public CanvasService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<Student> getAllStudentsInSystem() {
        List<Course> courses = getAllCourseIds();
        Map<String, Student> studentMap = new HashMap<>(); // Using Map to track students by ID
        Map<String, List<CourseEnrollment>> studentCourses = new HashMap<>(); // Track courses per student

        for (Course course : courses) {
            try {
                String endpoint = String.format("/api/v1/courses/%s/users?enrollment_type[]=student", course.getId());
                HttpEntity<?> entity = new HttpEntity<>(createHeaders());

                ResponseEntity<List<Student>> response = restTemplate.exchange(
                        canvasApiUrl + endpoint,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<Student>>() {}
                );

                if (response.getBody() != null) {
                    for (Student student : response.getBody()) {
                        // Update student record
                        studentMap.put(student.getId(), student);

                        // Create course enrollment for this student
                        CourseEnrollment enrollment = new CourseEnrollment();
                        enrollment.setCourseId(course.getId());
                        enrollment.setCourseName(course.getName());

                        // Add to student's course list
                        studentCourses
                                .computeIfAbsent(student.getId(), k -> new ArrayList<>())
                                .add(enrollment);
                    }
                }
            } catch (Exception e) {
                logger.warn("Error fetching students for course {}: {}", course.getId(), e.getMessage());
            }
        }

        // Set course lists for each student
        List<Student> uniqueStudents = new ArrayList<>();
        for (Student student : studentMap.values()) {
            student.setCourses(studentCourses.getOrDefault(student.getId(), new ArrayList<>()));
            uniqueStudents.add(student);
        }

        return uniqueStudents;
    }

    // Get courses for current user
    public List<CourseEnrollment> getCoursesForStudent(String studentId) {
        String endpoint = "/api/v1/users/self/courses";
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        try {
            ResponseEntity<List<CourseEnrollment>> response = restTemplate.exchange(
                    canvasApiUrl + endpoint,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CourseEnrollment>>() {}
            );

            if (response.getBody() != null) {
                return response.getBody().stream()
                        .map(course -> {
                            // Ensure no null values
                            CourseEnrollment enrollment = new CourseEnrollment();
                            enrollment.setCourseId(course.getCourseId() != null ? course.getCourseId() : "");
                            enrollment.setCourseName(course.getCourseName() != null ? course.getCourseName() : "Untitled Course");
                            return enrollment;
                        })
                        .collect(Collectors.toList());
            }
            return new ArrayList<>(); // Return empty list instead of null
        } catch (Exception e) {
            logger.error("Error fetching courses: ", e);
            throw new RuntimeException("Failed to fetch courses", e);
        }
    }

    public Map<String, Object> getStudentPerformance(String studentId) {
        Map<String, Object> performance = new HashMap<>();

        // Get courses and grades for self
        List<CourseGrade> grades = getGradesForStudent(studentId, null);
        List<CourseEnrollment> courses = getCoursesForStudent(studentId);

        // Calculate overall GPA
        double gpaSum = 0.0;
        int courseCount = 0;
        for (CourseGrade grade : grades) {
            gpaSum += grade.getGrade();
            courseCount++;
        }
        double averageGpa = courseCount > 0 ? gpaSum / courseCount : 0.0;

        performance.put("totalCourses", courseCount);
        performance.put("averageGPA", averageGpa);
        performance.put("courseGrades", grades);
        performance.put("enrolledCourses", courses);

        return performance;
    }

    public List<CourseGrade> getGradesForStudent(String studentId, String courseId) {
        String endpoint = "/api/v1/users/self/courses?include[]=total_scores";
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    canvasApiUrl + endpoint,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<CourseGrade> grades = new ArrayList<>();
            if (response.getBody() != null) {
                for (Map<String, Object> courseData : response.getBody()) {
                    CourseGrade grade = new CourseGrade();
                    grade.setCourseId(Optional.ofNullable(courseData.get("id"))
                            .map(Object::toString)
                            .orElse(""));
                    grade.setCourseName(Optional.ofNullable(courseData.get("name"))
                            .map(Object::toString)
                            .orElse("Untitled Course"));
                    grade.setGrade(0.0); // Default grade

                    if (courseData.containsKey("enrollments") &&
                            !((List<?>) courseData.get("enrollments")).isEmpty()) {
                        Map<String, Object> enrollment =
                                (Map<String, Object>) ((List<?>) courseData.get("enrollments")).get(0);

                        if (enrollment.containsKey("computed_current_score")) {
                            Object scoreObj = enrollment.get("computed_current_score");
                            if (scoreObj != null) {
                                try {
                                    double gradeValue = Double.parseDouble(scoreObj.toString());
                                    grade.setGrade(gradeValue);
                                } catch (NumberFormatException e) {
                                    logger.warn("Invalid grade value for course {}", grade.getCourseId());
                                }
                            }
                        }
                    }
                    grades.add(grade);
                }
            }
            return grades;
        } catch (Exception e) {
            logger.error("Error fetching grades: ", e);
            throw new RuntimeException("Failed to fetch grades", e);
        }
    }

    public Student getStudentDetails(String studentId) {
        // Get student details from course enrollments
        List<Course> courses = getAllCourseIds();
        Student student = null;

        for (Course course : courses) {
            try {
                String endpoint = String.format("/api/v1/courses/%s/users?enrollment_type[]=student", course.getId());
                HttpEntity<?> entity = new HttpEntity<>(createHeaders());

                ResponseEntity<List<Student>> response = restTemplate.exchange(
                        canvasApiUrl + endpoint,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<Student>>() {}
                );

                if (response.getBody() != null) {
                    Optional<Student> foundStudent = response.getBody().stream()
                            .filter(s -> s.getId().equals(studentId))
                            .findFirst();

                    if (foundStudent.isPresent()) {
                        student = foundStudent.get();
                        break;  // Found the student, no need to check other courses
                    }
                }
            } catch (Exception e) {
                logger.warn("Error fetching students for course {}: {}", course.getId(), e.getMessage());
            }
        }

        // If student is found, get their courses and grades
        if (student != null) {
            student.setCourses(getStudentCourses(studentId));
            student.setCourseGrades(getStudentGrades(studentId));
            return student;
        } else {
            logger.error("Student with ID {} not found in any course", studentId);
            throw new RuntimeException("Student not found");
        }
    }

    private List<CourseEnrollment> getStudentCourses(String studentId) {
        List<Course> allCourses = getAllCourseIds();
        List<CourseEnrollment> studentCourses = new ArrayList<>();

        for (Course course : allCourses) {
            try {
                String endpoint = String.format("/api/v1/courses/%s/users?enrollment_type[]=student", course.getId());
                HttpEntity<?> entity = new HttpEntity<>(createHeaders());

                ResponseEntity<List<Student>> response = restTemplate.exchange(
                        canvasApiUrl + endpoint,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<Student>>() {}
                );

                if (response.getBody() != null &&
                        response.getBody().stream().anyMatch(s -> s.getId().equals(studentId))) {
                    CourseEnrollment enrollment = new CourseEnrollment();
                    enrollment.setCourseId(course.getId());
                    enrollment.setCourseName(course.getName() != null ? course.getName() : "Untitled Course");
                    studentCourses.add(enrollment);
                }
            } catch (Exception e) {
                logger.warn("Error checking course {} for student {}: {}", course.getId(), studentId, e.getMessage());
            }
        }
        return studentCourses;
    }

    private List<CourseGrade> getStudentGrades(String studentId) {
        List<CourseEnrollment> courses = getStudentCourses(studentId);
        List<CourseGrade> grades = new ArrayList<>();

        for (CourseEnrollment course : courses) {
            try {
                String endpoint = String.format("/api/v1/courses/%s/users/%s?include[]=total_scores",
                        course.getCourseId(), studentId);
                HttpEntity<?> entity = new HttpEntity<>(createHeaders());

                ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                        canvasApiUrl + endpoint,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Map<String, Object>>() {}
                );

                if (response.getBody() != null) {
                    CourseGrade grade = new CourseGrade();
                    grade.setCourseId(course.getCourseId());
                    grade.setCourseName(course.getCourseName());
                    grade.setGrade(0.0); // Default grade

                    List<?> enrollments = (List<?>) response.getBody().get("enrollments");
                    if (enrollments != null && !enrollments.isEmpty()) {
                        Map<String, Object> enrollment = (Map<String, Object>) enrollments.get(0);
                        if (enrollment.containsKey("computed_current_score")) {
                            Object scoreObj = enrollment.get("computed_current_score");
                            if (scoreObj != null) {
                                try {
                                    double gradeValue = Double.parseDouble(scoreObj.toString());
                                    grade.setGrade(gradeValue);
                                } catch (NumberFormatException e) {
                                    logger.warn("Invalid grade value for course {}", grade.getCourseId());
                                }
                            }
                        }
                    }
                    grades.add(grade);
                }
            } catch (Exception e) {
                logger.warn("Error fetching grades for course {} and student {}: {}",
                        course.getCourseId(), studentId, e.getMessage());
            }
        }
        return grades;
    }

    // Get courses for current user
    public List<Course> getAllCourseIds() {
        String endpoint = "/api/v1/courses";
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        try {
            ResponseEntity<List<Course>> response = restTemplate.exchange(
                    canvasApiUrl + endpoint,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Course>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error fetching courses: ", e);
            throw new RuntimeException("Failed to fetch courses", e);
        }
    }

    // Get enrolled students for a specific course if user has permission
    public List<Student> getAllStudentsInCourse(String courseId) {
        String endpoint = String.format("/api/v1/courses/%s/users?enrollment_type[]=student", courseId);
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        try {
            ResponseEntity<List<Student>> response = restTemplate.exchange(
                    canvasApiUrl + endpoint,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Student>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error fetching students for course {}: ", courseId, e);
            throw new RuntimeException("Failed to fetch course students", e);
        }
    }

}
