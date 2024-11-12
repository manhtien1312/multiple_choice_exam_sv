package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.ExamResultDTO;

import java.util.List;
import java.util.UUID;

public interface ExamResultService {
    List<ExamResultDTO> getResultsByExam(UUID examId);
    void exportResultsToExcel(UUID examId) throws Exception;
}
