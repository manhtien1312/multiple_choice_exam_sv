package com.graduationproject.exam_supervision_server.dto.request;

import com.graduationproject.exam_supervision_server.model.Question;

public record ChangeQuestionRequest(
        String examQuestionId,
        Question oldQuestion,
        Question newQuestion
) {
}
