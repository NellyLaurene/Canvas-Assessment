package com.example.canvasintegration.controller;

import com.example.canvasintegration.model.Student;
import com.example.canvasintegration.service.CanvasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for React app
public class StudentController {

    @Autowired
    private CanvasService canvasService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(canvasService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        return ResponseEntity.ok(canvasService.getStudentById(id));
    }
}
