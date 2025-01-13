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
@CrossOrigin(origins = "http://localhost:3000") // Adjust if necessary
public class StudentController {

    @Autowired
    private CanvasService canvasService;

    @GetMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<List<Map<String, Object>>> getQuizSubmissions(
            @RequestParam String courseId,
            @PathVariable String quizId) {
        List<Map<String, Object>> submissions = canvasService.getQuizSubmissions(courseId, quizId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Map<String, Object>>> getCourses() {
        List<Map<String, Object>> courses = canvasService.getCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents(@RequestParam String courseId) {
        List<Student> students = canvasService.getAllStudentsWithRanks(courseId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<Student> getStudentDetails(@PathVariable String studentId, @RequestParam String courseId) {
        Student student = canvasService.getStudentDetailedReport(studentId, courseId);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<List<Map<String, Object>>> getStudentCourses(@PathVariable String studentId) {
        List<Map<String, Object>> courses = canvasService.getStudentCourses(studentId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/students/{studentId}/average-grade")
    public ResponseEntity<Double> getStudentAverageGrade(@PathVariable String studentId, @RequestParam String courseId) {
        Student student = canvasService.getStudentDetailedReport(studentId, courseId);
        if (student != null) {
            double averageGrade = student.getAverageGrade();
            return ResponseEntity.ok(averageGrade);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
