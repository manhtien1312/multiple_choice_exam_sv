package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    ResponseEntity<Page<Student>> getAllStudents(int pageNumber);
    ResponseEntity<List<String>> getAllCohort();
    ResponseEntity<Page<Student>> filterStudent(String searchText, String majorName, String cohort, int pageNumber);
    ResponseEntity<MessageResponse> addStudentToClass(String classId, Student student);
    ResponseEntity<MessageResponse> addNewStudent(Student student);
    ResponseEntity<MessageResponse> addStudentByFile(MultipartFile studentFile) throws IOException;
    ResponseEntity<MessageResponse> addStudentToClassByFile(String classId, MultipartFile studentFile) throws IOException;
    ResponseEntity<MessageResponse> updateStudent(Student student);
    ResponseEntity<MessageResponse> removeStudentFromClass(String classId, List<String> selectedStudents);
    ResponseEntity<MessageResponse> deleteStudent(List<String> selectedStudents);

}
