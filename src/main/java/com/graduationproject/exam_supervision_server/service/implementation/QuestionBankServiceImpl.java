package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.QuestionBankDto;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.repository.QuestionBankRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.QuestionBankService;
import com.graduationproject.exam_supervision_server.utils.dtomapper.QuestionBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private QuestionBankMapper questionBankMapper;

    @Override
    public ResponseEntity<QuestionBankDto> getBySubjectId(String id, int type) {
        Optional<QuestionBank> qb = questionBankRepository.findBySubjectId(UUID.fromString(id), type);
        if (qb.isEmpty()){
            throw new RuntimeException("Ngân hàng không tồn tại, kiểm tra lại ID của môn học");
        }
        else {
           return ResponseEntity.status(HttpStatus.OK).body(qb.map(questionBankMapper).get());
        }
    }
}
