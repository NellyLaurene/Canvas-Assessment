package com.example.canvasintegration.controller;

import com.example.canvasintegration.model.Student;
import com.example.canvasintegration.service.CanvasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow cross-origin requests for testing; restrict in production.
public class StudentController {

    @Autowired
    private CanvasService canvasService;

    @GetMapping("/courses")
    public ResponseEntity<List<Map<String, Object>>> getCourses() {
        List<Map<String, Object>> courses = canvasService.getCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<List<Map<String, Object>>> getQuizSubmissions(
            @RequestParam String courseId,
            @PathVariable String quizId) {
        List<Map<String, Object>> submissions = canvasService.getQuizSubmissions(courseId, quizId);
        return ResponseEntity.ok(submissions);
    }
}
