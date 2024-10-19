package com.graduationproject.exam_supervision_server.service.serviceinterface;


import com.graduationproject.exam_supervision_server.model.ExamResult;

import java.io.IOException;
import java.util.List;

public interface ExamResultServiceInterface {
    List<ExamResult> getResultsByExam(Long examId);

    void exportResultsToExcel(Long examId) throws IOException;
}
