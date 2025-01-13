package com.example.canvasintegration.controller;

import com.example.canvasintegration.model.Course;
import com.example.canvasintegration.service.CanvasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Allow CORS for the frontend running on Vite
public class StudentController {

    @Autowired
    private CanvasService canvasService;

    // Endpoint to fetch all students
    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(@RequestParam String courseId) {
        try {
            return ResponseEntity.ok(canvasService.getAllStudents(courseId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching students: " + e.getMessage());
        }
    }

    // Endpoint to fetch a specific student by ID
    @GetMapping("/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id, @RequestParam String courseId) {
        try {
            return ResponseEntity.ok(canvasService.getStudentById(courseId, id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching student: " + e.getMessage());
        }
    }

    // Endpoint to fetch quiz submissions
    @GetMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<?> getQuizSubmissions(@PathVariable String quizId, @RequestParam String courseId) {
        try {
            return ResponseEntity.ok(canvasService.getQuizSubmissions(courseId, quizId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching quiz submissions: " + e.getMessage());
        }
    }

    // New endpoint to fetch published courses
    @GetMapping("/courses")
    public ResponseEntity<?> getPublishedCourses() {
        try {
            List<Course> courses = canvasService.getPublishedCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching courses: " + e.getMessage());
        }
    }
}
