package com.graduationproject.exam_supervision_server.dto.request;

import com.graduationproject.exam_supervision_server.model.Exam;
import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import com.graduationproject.exam_supervision_server.model.Student;

import java.util.List;

public record CreateExamRequest(
        Exam exam,
        List<Student> allowedStudents,
        List<ExamQuestion> selectedExamQuestions
) {
}
