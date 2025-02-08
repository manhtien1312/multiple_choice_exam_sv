package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionService {


    ResponseEntity<?>getQuestionById(String id);
    ResponseEntity<?> filterQuestion(String questionBankId, String typeName, int level, String searchText);
    ResponseEntity<String> getNewestQuestionCode(String typeName, String questionBankId);
    ResponseEntity<MessageResponse> addQuestion(String questionBankId, String questionStr, MultipartFile questionImage);
    ResponseEntity<MessageResponse> addThroughFile(String subjectId, MultipartFile questionFile);
    ResponseEntity<MessageResponse> modifyQuestion(String id, String questionStr, MultipartFile questionImage);
    ResponseEntity<MessageResponse> deleteQuestion(String id);


    // NV Ngọc
    List<Question> getPracticeQuestionsBySubject(String subjectId);

    boolean checkAnswer(String questionId, String selectedAnswer);

    String getExplanation(String questionId);

}
