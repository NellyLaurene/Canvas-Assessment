package com.example.canvasintegration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QuizSubmissionsResponse {

    @JsonProperty("quiz_submissions")
    private List<QuizSubmission> quizSubmissions;

    public List<QuizSubmission> getQuizSubmissions() {
        return quizSubmissions;
    }

    public void setQuizSubmissions(List<QuizSubmission> quizSubmissions) {
        this.quizSubmissions = quizSubmissions;
    }
}
