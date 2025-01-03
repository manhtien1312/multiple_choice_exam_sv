package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.ClassDto;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Class;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClassService {

    ResponseEntity<List<ClassDto>> getAllClasses();
    ResponseEntity<List<ClassDto>> getAllClassByTeacher();
    ResponseEntity<List<ClassDto>> getAllClassByStudent();
    ResponseEntity<?> getAllClassBySubject(String subjectId);
    ResponseEntity<Class> getClassById(String classId);
    void generateListStudentExcel(String classId, HttpServletResponse response) throws IOException;
    ResponseEntity<List<ClassDto>> searchClass(String searchText);
    ResponseEntity<MessageResponse> createClass(String subjectName, MultipartFile classFile) throws IOException;
    ResponseEntity<MessageResponse> deleteClass(String classId);

}
