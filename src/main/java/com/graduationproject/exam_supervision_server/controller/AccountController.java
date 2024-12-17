package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.service.serviceinterface.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String role
    ){
        return accountService.changePassword(oldPassword, newPassword, role);
    }

}
