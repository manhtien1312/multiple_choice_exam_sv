package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.request.ChangeQuestionRequest;
import com.graduationproject.exam_supervision_server.dto.request.CreateExamQuestionRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import org.springframework.http.ResponseEntity;

public interface ExamQuestionService {

    ResponseEntity<MessageResponse> createExamQuestion(CreateExamQuestionRequest request);
    ResponseEntity<?> getAllExamQuestionBySubject(String subjectId);
    ResponseEntity<?> getExamQuestionById(String examQuestionId);
    ResponseEntity<MessageResponse> changeQuestion(ChangeQuestionRequest request);
    ResponseEntity<MessageResponse> deleteExamQuestion(String id);

}
