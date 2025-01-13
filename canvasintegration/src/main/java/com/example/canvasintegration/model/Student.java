package com.example.canvasintegration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {
    private String id;
    private String name;
    private String email;
    private Integer rank;
    private Map<String, Object> enrollments;
    private Map<String, Object> grades;  // This will hold the grades for various assignments or subjects

    // Getters and setters
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Map<String, Object> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Map<String, Object> enrollments) {
        this.enrollments = enrollments;
    }

    public Map<String, Object> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Object> grades) {
        this.grades = grades;
    }

    // Method to calculate the average grade
    public double getAverageGrade() {
        if (grades == null || grades.isEmpty()) {
            return 0.0;  // Return 0.0 if no grades are available
        }

        double total = 0.0;
        int count = 0;

        // Iterate over the grades map and sum the grades
        for (Object grade : grades.values()) {
            if (grade instanceof Number) {
                total += ((Number) grade).doubleValue();  // Add the numeric grade value to total
                count++;
            }
        }

        // If there are no numeric grades, return 0.0
        if (count == 0) {
            return 0.0;
        }

        return total / count;  // Return the average grade
    }
}
