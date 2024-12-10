package com.graduationproject.exam_supervision_server.dto.request;

public record SignUpRequest(
        String username,
        String password,
        String role
) {
}
