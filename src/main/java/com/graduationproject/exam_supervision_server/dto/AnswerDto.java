package com.graduationproject.exam_supervision_server.dto;

import java.util.UUID;

public record AnswerDto(
        UUID id,
        String answerContent
) {
}
