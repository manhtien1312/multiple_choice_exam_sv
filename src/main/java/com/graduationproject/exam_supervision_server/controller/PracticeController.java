package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.service.serviceinterface.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practice")
public class PracticeController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Question>> getPracticeQuestions(@PathVariable String subjectId) {
        List<Question> questions = questionService.getPracticeQuestionsBySubject(subjectId);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/question/{questionId}/check")
    public ResponseEntity<Boolean> checkAnswer(@PathVariable String questionId, @RequestBody String selectedAnswer) {
        boolean isCorrect = questionService.checkAnswer(questionId, selectedAnswer);
        return ResponseEntity.ok(isCorrect);
    }

    @GetMapping("/question/{questionId}/explanation")
    public ResponseEntity<String> getExplanation(@PathVariable String questionId) {
        String explanation = questionService.getExplanation(questionId);
        return ResponseEntity.ok(explanation);
    }
}
