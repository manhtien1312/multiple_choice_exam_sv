package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Major;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MajorService {

    ResponseEntity<List<Major>> getAllMajors();
    ResponseEntity<MessageResponse> addMajor(Major major);

}
