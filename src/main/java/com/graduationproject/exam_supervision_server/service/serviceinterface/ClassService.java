package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.ClassDto;
import com.graduationproject.exam_supervision_server.dto.request.ClassQueryParam;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Class;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClassService {

    ResponseEntity<List<ClassDto>> getAllClassByTeacher();
    ResponseEntity<Class> getClassById(String classId);
    ResponseEntity<List<ClassDto>> searchClass(ClassQueryParam queryParam);
    ResponseEntity<MessageResponse> createClass(ClassDto classDto);
    ResponseEntity<MessageResponse> deleteClass(String classId);

}
