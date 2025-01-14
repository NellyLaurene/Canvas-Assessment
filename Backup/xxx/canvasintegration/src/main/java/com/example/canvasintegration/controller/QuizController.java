package com.example.canvasintegration.controller;

import com.example.canvasintegration.model.QuizSubmission;
import com.example.canvasintegration.service.CanvasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for React app
public class QuizController {

    private final CanvasService canvasService;

    public QuizController(CanvasService canvasService) {
        this.canvasService = canvasService;
    }

    @GetMapping("/{quizId}/submissions")
    public ResponseEntity<List<QuizSubmission>> getQuizSubmissions(
            @PathVariable String quizId,
            @RequestParam String courseId) {
        List<QuizSubmission> submissions = canvasService.getQuizSubmissions(courseId, quizId);
        return ResponseEntity.ok(submissions);
    }
}
