package com.graduationproject.exam_supervision_server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassStatisticsDTO {
    private double averageScore;
    private int totalStudents;
    private int totalPass;
    private int totalFail;
    private Map<UUID, Double> studentScores;
}
