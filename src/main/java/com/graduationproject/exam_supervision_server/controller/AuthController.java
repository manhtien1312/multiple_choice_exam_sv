package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.request.LoginRequest;
import com.graduationproject.exam_supervision_server.dto.request.SignUpRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.service.serviceinterface.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register-admin")
    public ResponseEntity<MessageResponse> registerAdmin(@RequestBody SignUpRequest request){
        return accountService.registerAdmin(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return accountService.login(request);
    }

}
