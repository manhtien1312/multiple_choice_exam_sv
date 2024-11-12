package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.ExamResultDTO;
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
    public ResponseEntity<List<ExamResultDTO>> getExamResults(@PathVariable String examId) {
        try {
            UUID examUUID = UUID.fromString(examId);  // Chuyển đổi examId từ String thành UUID
            List<ExamResultDTO> results = examResultService.getResultsByExam(examUUID);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/exam/{examId}/export")
    public ResponseEntity<String> exportExamResults(@PathVariable String examId) {
        try {
            UUID examUUID = UUID.fromString(examId);  // Chuyển đổi examId từ String thành UUID
            examResultService.exportResultsToExcel(examUUID);
            return ResponseEntity.ok("Results exported successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to export results.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid examId format.");
        }
    }
}
