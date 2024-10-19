package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.model.ExamResult;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ExamResultServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ExamResultServiceInterface examResultService;

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamResult>> getExamResults(@PathVariable String examId) {
        List<ExamResult> results = examResultService.getResultsByExam(examId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/exam/{examId}/export")
    public ResponseEntity<String> exportExamResults(@PathVariable String examId) {
        try {
            examResultService.exportResultsToExcel(examId);
            return ResponseEntity.ok("Results exported successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to export results.");
        }
    }
}
