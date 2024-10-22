package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.request.SignUpRequest;
import com.graduationproject.exam_supervision_server.model.Account;
import com.graduationproject.exam_supervision_server.model.ERole;
import com.graduationproject.exam_supervision_server.model.Role;
import com.graduationproject.exam_supervision_server.repository.AccountRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.AccountService;
import com.graduationproject.exam_supervision_server.service.serviceinterface.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Account register(SignUpRequest request) throws Exception {
        String requestRole = request.role();
        Role role = new Role();

        if(requestRole == null){
            role = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow();
        }
        else {
            switch (requestRole){
                case "admin":
                    role = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow();
                case "teacher":
                    role = roleService.findByRoleName(ERole.ROLE_TEACHER).orElseThrow();
                case "student":
                    role = roleService.findByRoleName(ERole.ROLE_STUDENT).orElseThrow();
            }
        }

        var account = Account.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();

        return account;
    }
}
