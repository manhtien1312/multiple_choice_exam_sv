package com.example.exam_supervision.controller;

import com.example.exam_supervision.model.ExamResult;
import com.example.exam_supervision.service.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ExamResultService examResultService;

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamResult>> getExamResults(@PathVariable Long examId) {
        List<ExamResult> results = examResultService.getResultsByExam(examId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/exam/{examId}/export")
    public ResponseEntity<String> exportExamResults(@PathVariable Long examId) {
        try {
            examResultService.exportResultsToExcel(examId);
            return ResponseEntity.ok("Results exported successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to export results.");
        }
    }
}
