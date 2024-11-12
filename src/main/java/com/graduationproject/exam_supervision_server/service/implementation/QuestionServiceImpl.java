package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.repository.QuestionBankRepository;
import com.graduationproject.exam_supervision_server.repository.QuestionRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Override
    public ResponseEntity<?> getQuestionById(UUID id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            return ResponseEntity.ok(question.get());
        } else {
            return ResponseEntity.status(404).body(new MessageResponse("Question not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addQuestion(String questionBankId, Question question) {
        Optional<QuestionBank> questionBank = questionBankRepository.findById(UUID.fromString(questionBankId));
        if (questionBank.isPresent()) {
            question.setQuestionBank(questionBank.get()); // Gán QuestionBank cho Question
            questionRepository.save(question);
            return ResponseEntity.ok(new MessageResponse("Question added successfully"));
        } else {
            return ResponseEntity.status(404).body(new MessageResponse("QuestionBank not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addThroughFile(String questionBankId, MultipartFile questionFile) {
        // Phương thức này sẽ xử lý thêm câu hỏi từ file, bạn có thể dùng Apache POI để đọc file Excel
        return ResponseEntity.ok(new MessageResponse("Questions added through file"));
    }

    @Override
    public ResponseEntity<MessageResponse> modifyQuestion(UUID id, Question question) {
        Optional<Question> existingQuestion = questionRepository.findById(id);
        if (existingQuestion.isPresent()) {
            Question updatedQuestion = existingQuestion.get();
            updatedQuestion.setQuestionContent(question.getQuestionContent());
            updatedQuestion.setExplanation(question.getExplanation());
            updatedQuestion.setAnswers(question.getAnswers());
            questionRepository.save(updatedQuestion);
            return ResponseEntity.ok(new MessageResponse("Question modified successfully"));
        } else {
            return ResponseEntity.status(404).body(new MessageResponse("Question not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteQuestion(UUID id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            questionRepository.delete(question.get());
            return ResponseEntity.ok(new MessageResponse("Question deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(new MessageResponse("Question not found"));
        }
    }
}
