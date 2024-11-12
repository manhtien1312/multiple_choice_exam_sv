package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.model.Question;
import java.util.List;
import java.util.UUID;

public interface PracticeService {

    // Lấy câu hỏi luyện tập từ ngân hàng câu hỏi (QuestionBank)
    List<Question> getPracticeQuestions(UUID questionBankId);

    // Phương thức kiểm tra câu trả lời của người dùng
    boolean checkAnswer(UUID questionId, String answer);
}
