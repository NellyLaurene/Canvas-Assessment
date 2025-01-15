package com.nelly.canvasintegration.model;

import java.util.List;
import java.util.Map;

public class Student {

    private String id;
    private String name;
    private String email;
    private List<CourseEnrollment> courses; // List of courses the student is enrolled in
    private List<CourseGrade> courseGrades; // Grades for each course
    private Map<String, Object> performanceMetrics;
    private Map<String, Object> engagementMetrics;

    // Getters and Setters for Basic Information
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CourseEnrollment> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseEnrollment> courses) {
        this.courses = courses;
    }

    public List<CourseGrade> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(List<CourseGrade> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public Map<String, Object> getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(Map<String, Object> performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public Map<String, Object> getEngagementMetrics() {
        return engagementMetrics;
    }

    public void setEngagementMetrics(Map<String, Object> engagementMetrics) {
        this.engagementMetrics = engagementMetrics;
    }

}
