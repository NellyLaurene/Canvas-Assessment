package com.nelly.canvasintegration.controller;

import com.nelly.canvasintegration.model.Student;
import com.nelly.canvasintegration.service.CanvasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/canvas")
public class StudentController {

    @Autowired
    private CanvasService canvasService;

    @GetMapping("/all-students")
    public ResponseEntity<?> getAllStudentsInSystem() {
        try {
            return ResponseEntity.ok(canvasService.getAllStudentsInSystem());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching students: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/courses")
    public ResponseEntity<?> getStudentCourses(@PathVariable String studentId) {
        try {
            return ResponseEntity.ok(canvasService.getCoursesForStudent(studentId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching courses for student: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/performance")
    public ResponseEntity<Map<String, Object>> getStudentPerformance(@PathVariable String studentId) {
        try {
            Map<String, Object> performance = canvasService.getStudentPerformance(studentId);
            return ResponseEntity.ok(performance);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student/{studentId}/grades")
    public ResponseEntity<?> getStudentGrades(@PathVariable String studentId) {
        try {
            return ResponseEntity.ok(canvasService.getGradesForStudent(studentId, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching grades for student: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/details")
    public ResponseEntity<?> getStudentDetails(@PathVariable String studentId) {
        try {
            Student student = canvasService.getStudentDetails(studentId);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching details for student: " + e.getMessage());
        }
    }


    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        try {
            return ResponseEntity.ok(canvasService.getAllCourseIds());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching courses: " + e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}/students")
    public ResponseEntity<?> getStudentsInCourse(@PathVariable String courseId) {
        try {
            return ResponseEntity.ok(canvasService.getAllStudentsInCourse(courseId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching students for course: " + e.getMessage());
        }
    }

}
