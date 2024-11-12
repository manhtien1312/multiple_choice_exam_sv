package com.graduationproject.exam_supervision_server.dto;

import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.Subject;

import java.util.List;
import java.util.UUID;

public record QuestionBankDto(
        UUID id,
        Subject subject,
        String type,
        List<Question> questions
) {
}