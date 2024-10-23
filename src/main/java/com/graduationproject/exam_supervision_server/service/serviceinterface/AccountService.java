package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.request.LoginRequest;
import com.graduationproject.exam_supervision_server.dto.request.SignUpRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Account;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    ResponseEntity<MessageResponse> registerAdmin(SignUpRequest request);
    Account register(SignUpRequest request);
    ResponseEntity<?> login(LoginRequest request);

}
