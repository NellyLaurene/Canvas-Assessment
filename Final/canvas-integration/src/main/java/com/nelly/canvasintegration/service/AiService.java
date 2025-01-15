package com.nelly.canvasintegration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import com.nelly.canvasintegration.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AiService {

    @Value("${ai-claude.key}")
    private String aiClaudeKey;

    private final CanvasService canvasService;
    private final ObjectMapper objectMapper;

    public AiService(CanvasService canvasService, ObjectMapper objectMapper) {
        this.canvasService = canvasService;
        this.objectMapper = objectMapper;
    }

    private String buildAnalysisPrompt(Student student) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("System: You are an AI assistant that analyzes student performance data and provides insights. " +
                "Return only the JSON response without any additional text. The response should include detailed " +
                "analysis in the following format:\n");

        prompt.append("{\n" +
                "  \"studentInfo\": {basic student information},\n" +
                "  \"performanceMetrics\": {current performance analysis},\n" +
                "  \"engagementAnalysis\": {engagement metrics},\n" +
                "  \"riskAssessment\": {risk level and factors},\n" +
                "  \"recommendations\": {suggested improvements}\n" +
                "}\n\n");

        prompt.append("Human: Analyze the following student data and provide insights:\n");

        try {
            prompt.append(objectMapper.writeValueAsString(student));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        prompt.append("\nAssistant: ");
        return prompt.toString();
    }

    public Map<String, Object> analyzeStudentPerformance(String studentId) {
        // Get student data
        Student student = canvasService.getStudentDetails(studentId);
        Map<String, Object> performance = canvasService.getStudentPerformance(studentId);
        List<CourseGrade> grades = student.getCourseGrades();

        // Enrich student data with additional analytics
        student.setPerformanceMetrics(calculatePerformanceMetrics(grades));
        student.setEngagementMetrics(calculateEngagementMetrics(student.getCourses()));

        // Generate AI analysis
        AnthropicChatModel anthropicChatModel = AnthropicChatModel.builder()
                .apiKey(aiClaudeKey)
                .modelName("claude-3-sonnet-20240229")
                .temperature(0.7)
                .build();

        String prompt = buildAnalysisPrompt(student);
        String response = anthropicChatModel.generate(prompt);

        // Parse and return the analysis
        try {
            return objectMapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> calculatePerformanceMetrics(List<CourseGrade> grades) {
        Map<String, Object> metrics = new HashMap<>();

        if (grades != null && !grades.isEmpty()) {
            // Calculate average grade
            double avgGrade = grades.stream()
                    .mapToDouble(CourseGrade::getGrade)
                    .average()
                    .orElse(0.0);

            // Find highest and lowest performing courses
            CourseGrade highestGrade = grades.stream()
                    .max(Comparator.comparing(CourseGrade::getGrade))
                    .orElse(null);

            CourseGrade lowestGrade = grades.stream()
                    .min(Comparator.comparing(CourseGrade::getGrade))
                    .orElse(null);

            metrics.put("averageGrade", avgGrade);
            metrics.put("highestPerformingCourse", highestGrade);
            metrics.put("lowestPerformingCourse", lowestGrade);
        }

        return metrics;
    }

    private Map<String, Object> calculateEngagementMetrics(List<CourseEnrollment> courses) {
        Map<String, Object> metrics = new HashMap<>();

        if (courses != null) {
            metrics.put("totalEnrolledCourses", courses.size());

            // Group courses by subject/category if available
            Map<String, Long> courseDistribution = courses.stream()
                    .collect(Collectors.groupingBy(
                            course -> course.getCourseName().split(" ")[0], // Simple subject extraction
                            Collectors.counting()
                    ));

            metrics.put("courseDistribution", courseDistribution);
        }

        return metrics;
    }

}
