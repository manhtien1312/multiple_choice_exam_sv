package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.QuestionBankDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface QuestionBankService {

    ResponseEntity<QuestionBankDto> getBySubjectId(String id, int type);
    void generateExcel(String subjectId, HttpServletResponse response) throws IOException;

}
