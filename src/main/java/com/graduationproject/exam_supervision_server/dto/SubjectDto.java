package com.graduationproject.exam_supervision_server.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.UUID;

public record SubjectDto(
        UUID id,
        @NotBlank(message = "Mã môn học không được để trống!")
        String subjectCode,
        @NotBlank(message = "Tên môn học không được để trống")
        String subjectName
) {
}
