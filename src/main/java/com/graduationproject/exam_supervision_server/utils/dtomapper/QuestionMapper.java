package com.graduationproject.exam_supervision_server.utils.dtomapper;

import com.graduationproject.exam_supervision_server.dto.AnswerDto;
import com.graduationproject.exam_supervision_server.dto.QuestionDto;
import com.graduationproject.exam_supervision_server.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class QuestionMapper implements Function<Question, QuestionDto> {

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public QuestionDto apply(Question question) {

        List<AnswerDto> answers = question.getAnswers().stream()
                .map(answerMapper)
                .toList();

        return new QuestionDto(
                question.getId(),
                question.getType().getTypeName(),
                question.getQuestionCode(),
                question.getQuestionContent(),
                answers,
                question.getExplanation()
        );
    }
}
