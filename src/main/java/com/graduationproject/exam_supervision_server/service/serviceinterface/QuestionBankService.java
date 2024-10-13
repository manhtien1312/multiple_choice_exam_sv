package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.QuestionBankDto;
import org.springframework.http.ResponseEntity;

public interface QuestionBankService {

    ResponseEntity<QuestionBankDto> getBySubjectId(String id, int type);

}
