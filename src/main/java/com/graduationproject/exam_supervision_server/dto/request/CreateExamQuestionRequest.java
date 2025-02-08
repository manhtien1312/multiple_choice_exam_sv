package com.graduationproject.exam_supervision_server.dto.request;

import com.graduationproject.exam_supervision_server.model.Subject;
import com.graduationproject.exam_supervision_server.utils.ExamQuestionDetail;

import java.util.List;

public record CreateExamQuestionRequest(
        String examQuestionCode,
        Integer totalQuestions,
        String subjectId,
        List<ExamQuestionDetail> details
) {
}
