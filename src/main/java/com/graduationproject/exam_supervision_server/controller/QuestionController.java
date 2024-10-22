package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.service.serviceinterface.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable String id){
        return questionService.getQuestionById(id);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addQuestion(@RequestParam String questionBankId, @RequestBody Question question){
        return questionService.addQuestion(questionBankId, question);
    }

    @PostMapping("/add-file")
    public ResponseEntity<MessageResponse> addThroughFile(@RequestParam String subjectId, @RequestParam MultipartFile questionFile){
        return questionService.addThroughFile(subjectId, questionFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> modifyQuestion(@PathVariable String id, @RequestBody Question question){
        return questionService.modifyQuestion(id, question);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteQuestion(@PathVariable String id){
        return questionService.deleteQuestion(id);
    }

}
