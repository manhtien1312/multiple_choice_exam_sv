package com.graduationproject.exam_supervision_server.utils.dtomapper;

import com.graduationproject.exam_supervision_server.dto.ExamResultDTO;
import com.graduationproject.exam_supervision_server.model.ExamResult;
import org.springframework.stereotype.Component;

@Component
public class ExamResultMapper {

    // Phương thức toDTO để chuyển đổi từ ExamResult sang ExamResultDTO
    public ExamResultDTO toDTO(ExamResult examResult) {
        return ExamResultDTO.builder()
                .examId(examResult.getExam().getId()) // Lấy examId từ đối tượng ExamResult
                .studentId(examResult.getStudent().getId()) // Lấy studentId từ đối tượng ExamResult
                .correctQuestions(examResult.getCorrectQuestions())
                .wrongQuestions(examResult.getWrongQuestions())
                .blankQuestions(examResult.getBlankQuestions())
                .score(examResult.getScore())
                .build();
    }
}
