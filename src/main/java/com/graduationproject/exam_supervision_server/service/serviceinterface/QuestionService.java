package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface QuestionService {

    ResponseEntity<?> getQuestionById(UUID id);

    ResponseEntity<MessageResponse> addQuestion(String questionBankId, Question question);

    ResponseEntity<MessageResponse> addThroughFile(String questionBankId, MultipartFile questionFile);

    ResponseEntity<MessageResponse> modifyQuestion(UUID id, Question question);

    ResponseEntity<MessageResponse> deleteQuestion(UUID id);
}
