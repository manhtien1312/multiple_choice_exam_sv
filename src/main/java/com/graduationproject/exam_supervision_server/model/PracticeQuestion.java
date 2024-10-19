package com.graduationproject.exam_supervision_server.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class PracticeQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionContent;

    @ElementCollection
    private List<String> options;

    private String correctAnswer;
    private String explanation;

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer; // Getter cho correctAnswer
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer; // Setter cho correctAnswer
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
