package com.graduationproject.exam_supervision_server.utils.dtomapper;

import com.graduationproject.exam_supervision_server.dto.ClassStatisticsDTO;
import com.graduationproject.exam_supervision_server.model.ExamResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ClassStatisticsMapper {

    public ClassStatisticsDTO toDTO(UUID classId, String className, List<ExamResult> examResults) {
        double averageScore = examResults.stream().mapToDouble(ExamResult::getScore).average().orElse(0.0);
        int totalStudents = examResults.size();
        int totalPass = (int) examResults.stream().filter(result -> result.getScore() >= 5.0).count(); // Giả sử điểm >= 50 là đậu
        int totalFail = totalStudents - totalPass;
        Map<UUID, Double> studentScores = examResults.stream()
                .collect(Collectors.toMap(result -> result.getStudent().getId(), ExamResult::getScore));

        return ClassStatisticsDTO.builder()
                .averageScore(averageScore)
                .totalStudents(totalStudents)
                .totalPass(totalPass)
                .totalFail(totalFail)
                .studentScores(studentScores)
                .build();
    }
}
