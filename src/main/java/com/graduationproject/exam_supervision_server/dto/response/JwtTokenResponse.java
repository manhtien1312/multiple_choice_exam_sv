package com.graduationproject.exam_supervision_server.dto.response;

public record JwtTokenResponse(
        String token,
        String role
) {
}
