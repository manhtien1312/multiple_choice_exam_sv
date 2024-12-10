package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.request.ChangeQuestionRequest;
import com.graduationproject.exam_supervision_server.dto.request.CreateExamQuestionRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ExamQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/exam-question")
public class ExamQuestionController {

    @Autowired
    private ExamQuestionService examQuestionService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createExamQuestion(@RequestBody CreateExamQuestionRequest request){
        return examQuestionService.createExamQuestion(request);
    }

    @GetMapping
    public ResponseEntity<?> getAllBySubjectId(@RequestParam String subjectId){
        return examQuestionService.getAllExamQuestionBySubject(subjectId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExamQuestionById(@PathVariable String id){
        return examQuestionService.getExamQuestionById(id);
    }

    @PutMapping("change-question")
    public ResponseEntity<MessageResponse> changeQuestion(@RequestBody ChangeQuestionRequest request){
        return examQuestionService.changeQuestion(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteExamQuestion(@PathVariable String id){
        return examQuestionService.deleteExamQuestion(id);
    }

}
