package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.service.serviceinterface.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/add-to-class")
    public ResponseEntity<MessageResponse> addStudent(@RequestParam String classId, @RequestBody Student student){
        return studentService.addStudentToClass(classId, student);
    }

    @PostMapping("/add-student-file")
    public ResponseEntity<MessageResponse> addStudentByFile(@RequestParam String classId, @RequestParam MultipartFile studentFile) throws IOException {
        return studentService.addStudentToClassByFile(classId, studentFile);
    }

}
