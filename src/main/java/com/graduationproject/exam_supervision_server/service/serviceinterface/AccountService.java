package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.request.SignUpRequest;
import com.graduationproject.exam_supervision_server.model.Account;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    Account register(SignUpRequest request) throws Exception;

}
