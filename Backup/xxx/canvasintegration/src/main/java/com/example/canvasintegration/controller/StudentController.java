package com.example.canvasintegration.controller;

import com.example.canvasintegration.model.Student;
import com.example.canvasintegration.service.CanvasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for React app
public class StudentController {

    private final CanvasService canvasService;

    public StudentController(CanvasService canvasService) {
        this.canvasService = canvasService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudentsByCourse(@RequestParam String courseId) {
        List<Student> students = canvasService.getStudentsByCourseId(courseId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentDetails(
            @PathVariable String id,
            @RequestParam String courseId) {
        Student student = canvasService.getStudentDetails(courseId, id);
        return ResponseEntity.ok(student);
    }
}
