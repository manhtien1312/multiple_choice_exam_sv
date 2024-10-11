package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.SubjectDto;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Subject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubjectService {

    ResponseEntity<?> getAllSubjects();
    ResponseEntity<?> getSubjectById(String id);
    ResponseEntity<MessageResponse> addSubject(SubjectDto subjectDto);
    ResponseEntity<MessageResponse> modifySubject(String id, SubjectDto subjectDto);
    ResponseEntity<MessageResponse> deleteSubject(String id);

}
