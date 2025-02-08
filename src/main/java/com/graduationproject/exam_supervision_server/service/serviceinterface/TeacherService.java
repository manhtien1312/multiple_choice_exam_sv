package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Teacher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeacherService {

    ResponseEntity<List<Teacher>> getAllTeacher();
    ResponseEntity<Teacher> getTeacherById(String teacherId);
    ResponseEntity<List<Teacher>> searchTeacher(String searchText);
    ResponseEntity<List<Teacher>> filterTeacher(String majorName);
    ResponseEntity<MessageResponse> addTeacher(Teacher teacher);
    ResponseEntity<MessageResponse> addTeacherByFile(MultipartFile teacherFile) throws IOException;
    ResponseEntity<MessageResponse> modifyTeacher(String teacherId, Teacher teacher);
    ResponseEntity<MessageResponse> deleteTeacherById(String teacherId);

}
