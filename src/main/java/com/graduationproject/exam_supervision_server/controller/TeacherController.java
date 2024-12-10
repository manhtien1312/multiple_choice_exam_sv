package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Teacher;
import com.graduationproject.exam_supervision_server.service.serviceinterface.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<?> getAllTeacher(){
        return teacherService.getAllTeacher();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable String id){
        return teacherService.getTeacherById(id);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addTeacher(@RequestBody Teacher teacher){
        return teacherService.addTeacher(teacher);
    }

    @PostMapping("/add-excel")
    public ResponseEntity<MessageResponse> addTeacherThroughFile(@RequestParam MultipartFile teacherFile) throws IOException {
        return teacherService.addTeacherByFile(teacherFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> modifyTeacher(@PathVariable String id, @RequestBody Teacher teacher){
        return teacherService.modifyTeacher(id, teacher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteTeacher(@PathVariable String id){
        return teacherService.deleteTeacherById(id);
    }

}
