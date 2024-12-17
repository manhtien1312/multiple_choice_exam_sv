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

    @GetMapping("/filter")
    public ResponseEntity<?> filterQuestion(
            @RequestParam String questionBankId,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String searchText
    ){
        return questionService.filterQuestion(questionBankId, typeName, level, searchText);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addQuestion(
            @RequestParam String questionBankId,
            @RequestParam String questionStr,
            @RequestParam(required = false) MultipartFile questionImage
    ){
        return questionService.addQuestion(questionBankId, questionStr, questionImage);
    }

    @PostMapping("/add-file")
    public ResponseEntity<MessageResponse> addThroughFile(@RequestParam String subjectId, @RequestParam MultipartFile questionFile){
        return questionService.addThroughFile(subjectId, questionFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> modifyQuestion(
            @PathVariable String id,
            @RequestParam String questionStr,
            @RequestParam(required = false) MultipartFile questionImage
    ){
        return questionService.modifyQuestion(id, questionStr, questionImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteQuestion(@PathVariable String id){
        return questionService.deleteQuestion(id);
    }

}
