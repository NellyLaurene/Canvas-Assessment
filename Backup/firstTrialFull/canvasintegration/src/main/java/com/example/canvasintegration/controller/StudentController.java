package com.example.canvasintegration.controller;

import com.example.canvasintegration.model.QuizSubmission;
import com.example.canvasintegration.model.Student;
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

    /**
     * Endpoint to fetch all students in a course
     *
     * @param courseId The course ID for which students need to be fetched
     * @return ResponseEntity containing the list of students or error details
     */
    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(@RequestParam String courseId) {
        try {
            List<Student> students = canvasService.getAllStudents(courseId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching students: " + e.getMessage());
        }
    }

    /**
     * Endpoint to fetch details of a specific student by ID
     *
     * @param id       The student ID
     * @param courseId The course ID in which the student is enrolled
     * @return ResponseEntity containing the student details or error details
     */
    @GetMapping("/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id, @RequestParam String courseId) {
        try {
            Student student = canvasService.getStudentById(courseId, id);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching student: " + e.getMessage());
        }
    }

    /**
     * Endpoint to fetch quiz submissions for a specific course and quiz
     *
     * @param quizId   The quiz ID for which submissions need to be fetched
     * @param courseId The course ID containing the quiz
     * @return ResponseEntity containing the list of quiz submissions or error details
     */
    @GetMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<?> getQuizSubmissions(
            @PathVariable String quizId,
            @RequestParam String courseId) {
        try {
            List<QuizSubmission> submissions = canvasService.getQuizSubmissions(courseId, quizId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching quiz submissions: " + e.getMessage());
        }
    }
}
