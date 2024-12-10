package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.model.QuestionType;
import com.graduationproject.exam_supervision_server.repository.QuestionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/question-type")
public class QuestionTypeController {

    @Autowired
    private QuestionTypeRepository questionTypeRepository;

    @GetMapping
    public ResponseEntity<List<QuestionType>> getAllType(@RequestParam String subjectId){
        List<QuestionType> res = questionTypeRepository.findAllBySubjectId(UUID.fromString(subjectId));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
