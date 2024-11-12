package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.service.serviceinterface.PracticeService;
import com.graduationproject.exam_supervision_server.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/practice")
public class PracticeController {

    @Autowired
    private PracticeService practiceService;

    // Lấy câu hỏi luyện tập từ ngân hàng câu hỏi
    @GetMapping("/questions/{questionBankId}")
    public ResponseEntity<List<Question>> getPracticeQuestions(@PathVariable UUID questionBankId) {
        List<Question> questions = practiceService.getPracticeQuestions(questionBankId);
        return ResponseEntity.ok(questions);
    }

    // Kiểm tra câu trả lời của người dùng
    @PostMapping("/check-answer/{questionId}")
    public ResponseEntity<MessageResponse> checkAnswer(@PathVariable UUID questionId, @RequestParam String answer) {
        boolean isCorrect = practiceService.checkAnswer(questionId, answer);
        String message = isCorrect ? "Answer is correct" : "Answer is incorrect";
        return ResponseEntity.ok(new MessageResponse(message));
    }
}
