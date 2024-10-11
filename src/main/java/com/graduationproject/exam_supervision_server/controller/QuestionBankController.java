package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.service.serviceinterface.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/question-bank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    @GetMapping
    public ResponseEntity<QuestionBank> getQuestionBankBySubjectId(@RequestParam String subjectId, @RequestParam int type){
        return questionBankService.getBySubjectId(subjectId, type);
    }

}
