package com.nelly.canvasintegration.controller;

import com.nelly.canvasintegration.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @GetMapping("/analysis/{studentId}")
    public ResponseEntity<?> getStudentAnalysis(@PathVariable String studentId) {
        try {
            Map<String, Object> analysis = aiService.analyzeStudentPerformance(studentId);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error analyzing student performance: " + e.getMessage());
        }
    }

}
