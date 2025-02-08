package com.graduationproject.exam_supervision_server.dto;

public record ClassDto(
        String id,
        String className,
        String subject,
        String teacherName
) {
}
