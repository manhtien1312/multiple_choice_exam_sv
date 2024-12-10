package com.graduationproject.exam_supervision_server.utils;

import com.graduationproject.exam_supervision_server.model.QuestionType;

public record ExamQuestionDetail(
        String typeName,
        Integer numberOfQuestions
) {
}
