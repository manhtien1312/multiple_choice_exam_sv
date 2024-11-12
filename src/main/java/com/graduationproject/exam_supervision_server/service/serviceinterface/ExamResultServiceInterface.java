package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.ExamResultDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ExamResultServiceInterface {

    List<ExamResultDTO> getResultsByExam(UUID examId);

    // Thêm khai báo ngoại lệ IOException trong interface
    void exportResultsToExcel(UUID examId) throws IOException;
}
