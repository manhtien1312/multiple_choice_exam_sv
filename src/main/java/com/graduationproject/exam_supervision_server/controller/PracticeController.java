package com.example.exam_supervision.controller;

import com.example.exam_supervision.model.PracticeQuestion;
import com.example.exam_supervision.service.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practice")
public class PracticeController {

    @Autowired
    private PracticeService practiceService;

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<PracticeQuestion>> getPracticeQuestions(@PathVariable Long subjectId) {
        List<PracticeQuestion> questions = practiceService.getPracticeQuestionsBySubject(subjectId);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/question/{questionId}/check")
    public ResponseEntity<Boolean> checkAnswer(@PathVariable Long questionId, @RequestBody String selectedAnswer) {
        boolean isCorrect = practiceService.checkAnswer(questionId, selectedAnswer);
        return ResponseEntity.ok(isCorrect);
    }

    @GetMapping("/question/{questionId}/explanation")
    public ResponseEntity<String> getExplanation(@PathVariable Long questionId) {
        String explanation = practiceService.getExplanation(questionId);
        return ResponseEntity.ok(explanation);
    }
}
