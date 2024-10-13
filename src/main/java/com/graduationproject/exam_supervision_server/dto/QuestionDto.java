package com.graduationproject.exam_supervision_server.dto;

import java.util.List;
import java.util.UUID;

public record QuestionDto(
        UUID id,
        String type,
        String questionContent,
        List<AnswerDto> answers,
        String explanation

) {
}
