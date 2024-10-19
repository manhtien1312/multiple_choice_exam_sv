package com.graduationproject.exam_supervision_server.utils.dtomapper;

import com.graduationproject.exam_supervision_server.dto.AnswerDto;
import com.graduationproject.exam_supervision_server.model.Answer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AnswerMapper implements Function<Answer, AnswerDto> {
    @Override
    public AnswerDto apply(Answer answer) {
        return new AnswerDto(
                answer.getId(),
                answer.getAnswerContent()
        );
    }
}
