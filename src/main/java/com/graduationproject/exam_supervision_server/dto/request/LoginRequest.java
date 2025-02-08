package com.graduationproject.exam_supervision_server.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
