package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.request.CreateExamRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Exam;
import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/teacher")
    public ResponseEntity<?> getAllExamByClassAndTeacher(@RequestParam String classId){
        return examService.getExamByClassAndTeacher(classId);
    }

    @GetMapping("/student")
    public ResponseEntity<?> getAllExamByClassAndStudent(@RequestParam String classId){
        return examService.getExamByClassAndStudent(classId);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createExam(@RequestBody CreateExamRequest request){
        return examService.createExam(request);
    }

}
