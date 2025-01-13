package com.example.canvasintegration.controller;

import com.example.canvasintegration.model.Student;
import com.example.canvasintegration.service.CanvasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000")  // Allow CORS for frontend at localhost:3000
public class StudentController {
    private final CanvasService canvasService;

    // Constructor-based dependency injection
    public StudentController(CanvasService canvasService) {
        this.canvasService = canvasService;
    }

    // Get all students along with their ranks
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(@RequestParam String courseId) {
        List<Student> students = canvasService.getAllStudentsWithRanks(courseId);  // Pass courseId to service
        return ResponseEntity.ok(students);  // Return students in a ResponseEntity with status 200
    }

    // Get detailed report for a specific student by ID
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentDetails(@PathVariable String studentId, @RequestParam String courseId) {
        Student student = canvasService.getStudentDetailedReport(studentId, courseId);  // Pass both studentId and courseId to service
        if (student != null) {
            return ResponseEntity.ok(student);  // Return student details if found
        } else {
            return ResponseEntity.notFound().build();  // Return 404 if student not found
        }
    }

    // Get courses for a specific student by ID
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<Map<String, Object>>> getStudentCourses(@PathVariable String studentId) {
        List<Map<String, Object>> courses = canvasService.getStudentCourses(studentId);  // Ensure service method is implemented
        return ResponseEntity.ok(courses);  // Return courses in a ResponseEntity with status 200
    }

    // Get average grade for a specific student (if you want to add this feature)
    @GetMapping("/{studentId}/average-grade")
    public ResponseEntity<Double> getStudentAverageGrade(@PathVariable String studentId, @RequestParam String courseId) {
        Student student = canvasService.getStudentDetailedReport(studentId, courseId);  // Pass both studentId and courseId to service
        if (student != null) {
            double averageGrade = student.getAverageGrade();  // Call getAverageGrade() method
            return ResponseEntity.ok(averageGrade);  // Return average grade
        } else {
            return ResponseEntity.notFound().build();  // Return 404 if student not found
        }
    }
}
