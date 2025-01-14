package com.example.canvasintegration.controller;

import com.example.canvasintegration.model.Course;
import com.example.canvasintegration.model.Quiz;
import com.example.canvasintegration.model.Student;
import com.example.canvasintegration.service.CanvasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for React app
public class CourseController {

    private final CanvasService canvasService;

    public CourseController(CanvasService canvasService) {
        this.canvasService = canvasService;
    }

    @GetMapping
    public ResponseEntity<List<Student.Course>> getAllCourses() {
        List<Student.Course> courses = canvasService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}/quizzes")
    public ResponseEntity<List<Quiz>> getQuizzesByCourse(@PathVariable String courseId) {
        List<Quiz> quizzes = canvasService.getQuizzesByCourseId(courseId);
        return ResponseEntity.ok(quizzes);
    }

}
