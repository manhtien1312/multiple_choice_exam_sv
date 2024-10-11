package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.model.QuestionBank;
import org.springframework.http.ResponseEntity;

public interface QuestionBankService {

    ResponseEntity<QuestionBank> getBySubjectId(String id, int type);

}
