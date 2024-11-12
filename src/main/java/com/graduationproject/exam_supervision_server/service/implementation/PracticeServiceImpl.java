package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.repository.QuestionRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PracticeServiceImpl implements PracticeService {

    @Autowired
    private QuestionRepository questionRepository;

    // Lấy câu hỏi luyện tập từ ngân hàng câu hỏi (QuestionBank)
    @Override
    public List<Question> getPracticeQuestions(UUID questionBankId) {
        // Lấy tất cả các câu hỏi từ QuestionBank
        List<Question> questions = questionRepository.findByQuestionBankId(questionBankId);

        // Tiến hành trả về danh sách câu hỏi, có thể sắp xếp ngẫu nhiên nếu cần thiết
        return questions.stream()
                .collect(Collectors.toList());
    }

    // Kiểm tra câu trả lời của người dùng
    @Override
    public boolean checkAnswer(UUID questionId, String answer) {
        // Lấy câu hỏi theo ID
        Question question = questionRepository.findById(questionId).orElse(null);

        if (question == null) {
            return false;  // Nếu không tìm thấy câu hỏi
        }

        // Kiểm tra câu trả lời (Giả sử chỉ có một câu trả lời đúng)
        return question.getAnswers().stream()
                .anyMatch(a -> a.getAnswerContent().equalsIgnoreCase(answer) && a.getIsCorrect());
    }
}
