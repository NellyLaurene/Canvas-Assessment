package com.nelly.canvasintegration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiData {
    private StudentInfo studentInfo;
    private PerformanceMetrics performanceMetrics;
    private EngagementAnalysis engagementAnalysis;
    private RiskAssessment riskAssessment;
    private List<Recommendation> recommendations;

    // Nested class for student information
    public static class StudentInfo {
        private String id;
        private String name;
        private String email;
        private Integer totalCourses;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Integer getTotalCourses() { return totalCourses; }
        public void setTotalCourses(Integer totalCourses) { this.totalCourses = totalCourses; }
    }

    // Nested class for performance metrics
    public static class PerformanceMetrics {
        private Double averageGrade;
        private CoursePerformance highestPerformingCourse;
        private CoursePerformance lowestPerformingCourse;
        private Map<String, Integer> gradeDistribution;

        public static class CoursePerformance {
            private String courseId;
            private String courseName;
            private Double grade;

            // Getters and Setters
            public String getCourseId() { return courseId; }
            public void setCourseId(String courseId) { this.courseId = courseId; }
            public String getCourseName() { return courseName; }
            public void setCourseName(String courseName) { this.courseName = courseName; }
            public Double getGrade() { return grade; }
            public void setGrade(Double grade) { this.grade = grade; }
        }

        // Getters and Setters
        public Double getAverageGrade() { return averageGrade; }
        public void setAverageGrade(Double averageGrade) { this.averageGrade = averageGrade; }
        public CoursePerformance getHighestPerformingCourse() { return highestPerformingCourse; }
        public void setHighestPerformingCourse(CoursePerformance course) { this.highestPerformingCourse = course; }
        public CoursePerformance getLowestPerformingCourse() { return lowestPerformingCourse; }
        public void setLowestPerformingCourse(CoursePerformance course) { this.lowestPerformingCourse = course; }
        public Map<String, Integer> getGradeDistribution() { return gradeDistribution; }
        public void setGradeDistribution(Map<String, Integer> distribution) { this.gradeDistribution = distribution; }
    }

    // Nested class for engagement analysis
    public static class EngagementAnalysis {
        private Integer totalEnrolledCourses;
        private Map<String, Long> courseDistribution;
        private String participationLevel;
        private Double consistencyScore;

        // Getters and Setters
        public Integer getTotalEnrolledCourses() { return totalEnrolledCourses; }
        public void setTotalEnrolledCourses(Integer total) { this.totalEnrolledCourses = total; }
        public Map<String, Long> getCourseDistribution() { return courseDistribution; }
        public void setCourseDistribution(Map<String, Long> distribution) { this.courseDistribution = distribution; }
        public String getParticipationLevel() { return participationLevel; }
        public void setParticipationLevel(String level) { this.participationLevel = level; }
        public Double getConsistencyScore() { return consistencyScore; }
        public void setConsistencyScore(Double score) { this.consistencyScore = score; }
    }

    // Nested class for risk assessment
    public static class RiskAssessment {
        private String overallRiskLevel;
        private List<RiskFactor> riskFactors;
        private List<String> earlyWarningSignals;

        public static class RiskFactor {
            private String factor;
            private String level;
            private String description;

            // Getters and Setters
            public String getFactor() { return factor; }
            public void setFactor(String factor) { this.factor = factor; }
            public String getLevel() { return level; }
            public void setLevel(String level) { this.level = level; }
            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
        }

        // Getters and Setters
        public String getOverallRiskLevel() { return overallRiskLevel; }
        public void setOverallRiskLevel(String level) { this.overallRiskLevel = level; }
        public List<RiskFactor> getRiskFactors() { return riskFactors; }
        public void setRiskFactors(List<RiskFactor> factors) { this.riskFactors = factors; }
        public List<String> getEarlyWarningSignals() { return earlyWarningSignals; }
        public void setEarlyWarningSignals(List<String> signals) { this.earlyWarningSignals = signals; }
    }

    // Nested class for recommendations
    public static class Recommendation {
        private String area;
        private String suggestion;
        private String priority;

        // Getters and Setters
        public String getArea() { return area; }
        public void setArea(String area) { this.area = area; }
        public String getSuggestion() { return suggestion; }
        public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }

    // Getters and Setters for main class
    public StudentInfo getStudentInfo() { return studentInfo; }
    public void setStudentInfo(StudentInfo info) { this.studentInfo = info; }
    public PerformanceMetrics getPerformanceMetrics() { return performanceMetrics; }
    public void setPerformanceMetrics(PerformanceMetrics metrics) { this.performanceMetrics = metrics; }
    public EngagementAnalysis getEngagementAnalysis() { return engagementAnalysis; }
    public void setEngagementAnalysis(EngagementAnalysis analysis) { this.engagementAnalysis = analysis; }
    public RiskAssessment getRiskAssessment() { return riskAssessment; }
    public void setRiskAssessment(RiskAssessment assessment) { this.riskAssessment = assessment; }
    public List<Recommendation> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; }
}
