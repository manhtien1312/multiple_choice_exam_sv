package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.request.LoginRequest;
import com.graduationproject.exam_supervision_server.dto.request.SignUpRequest;
import com.graduationproject.exam_supervision_server.dto.response.JwtTokenResponse;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Account;
import com.graduationproject.exam_supervision_server.model.ERole;
import com.graduationproject.exam_supervision_server.model.Role;
import com.graduationproject.exam_supervision_server.repository.AccountRepository;
import com.graduationproject.exam_supervision_server.security.JwtService;
import com.graduationproject.exam_supervision_server.service.serviceinterface.AccountService;
import com.graduationproject.exam_supervision_server.service.serviceinterface.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<MessageResponse> registerAdmin(SignUpRequest request) {
        Role role = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow();

        var account = Account.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();

        accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm tài khoản admin thành công"));
    }

    @Override
    public Account register(SignUpRequest request) {
        String requestRole = request.role();
        Role role = new Role();

        if(requestRole == null){
            role = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow();
        }
        else {
            switch (requestRole){
                case "admin":
                    role = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow();
                    break;
                case "teacher":
                    role = roleService.findByRoleName(ERole.ROLE_TEACHER).orElseThrow();
                    break;
                case "student":
                    role = roleService.findByRoleName(ERole.ROLE_STUDENT).orElseThrow();
                    break;
            }
        }

        var account = Account.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();

        return account;
    }

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Account account = accountRepository.findByUsername(request.username()).orElseThrow();
            String token = jwtService.generateToken(account);
            return ResponseEntity.status(HttpStatus.OK).body(new JwtTokenResponse(token, account.getRole().getRoleName().name()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Thông tin đăng nhập không chính xác!"));
        }
    }
}
