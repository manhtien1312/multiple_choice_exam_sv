package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.ExamRequest;
import com.graduationproject.exam_supervision_server.dto.ExamResponse;
import com.graduationproject.exam_supervision_server.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {
	
    @Autowired
    private ExamService examService;

    @PostMapping("/generate")
    public ResponseEntity<ExamResponse> generateExam(@RequestBody ExamRequest examRequest) {
        ExamResponse exam = examService.createExam(examRequest);
        return ResponseEntity.ok(exam);
    }
}
