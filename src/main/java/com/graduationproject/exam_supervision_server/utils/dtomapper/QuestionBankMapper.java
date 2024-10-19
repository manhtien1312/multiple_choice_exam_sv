package com.graduationproject.exam_supervision_server.utils.dtomapper;

import com.graduationproject.exam_supervision_server.dto.QuestionBankDto;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class QuestionBankMapper implements Function<QuestionBank, QuestionBankDto> {
    @Override
    public QuestionBankDto apply(QuestionBank questionBank) {
        String type = questionBank.getType() == 1 ? "Ngân hàng thi" : "Ngân hàng ôn tập";
        return new QuestionBankDto(
                questionBank.getId(),
                questionBank.getSubject(),
                type,
                questionBank.getQuestions()
        );
    }
}
