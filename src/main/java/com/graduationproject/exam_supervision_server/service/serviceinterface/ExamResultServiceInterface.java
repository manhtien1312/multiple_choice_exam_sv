package com.graduationproject.exam_supervision_server.service.serviceinterface;


import com.graduationproject.exam_supervision_server.model.ExamResult;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ExamResultServiceInterface {
    List<ExamResult> getResultsByExam(String examId);

    void exportResultsToExcel(String examId) throws IOException;
}
