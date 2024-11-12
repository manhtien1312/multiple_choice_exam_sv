package com.graduationproject.exam_supervision_server.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDTO {
    private UUID examId;
    private UUID studentId;
    private String studentName;
    private Integer correctQuestions;
    private Integer wrongQuestions;
    private Integer blankQuestions;
    private Double score;
}
