package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudentService {

    ResponseEntity<MessageResponse> addStudentToClass(String classId, Student student);
    ResponseEntity<MessageResponse> addStudentToClassByFile(String classId, MultipartFile studentFile) throws IOException;

}
