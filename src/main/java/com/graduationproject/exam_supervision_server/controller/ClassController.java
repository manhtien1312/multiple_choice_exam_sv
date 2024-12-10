package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.ClassDto;
import com.graduationproject.exam_supervision_server.dto.request.ClassQueryParam;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Class;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping
    public ResponseEntity<List<ClassDto>> getAllClasses(){
        return classService.getAllClasses();
    }

    @GetMapping("/teacher-class")
    public ResponseEntity<List<ClassDto>> getAllClassByTeacher(){
        return classService.getAllClassByTeacher();
    }

    @GetMapping("/student-class")
    public ResponseEntity<List<ClassDto>> getAllClassByStudent(){
        return classService.getAllClassByStudent();
    }

    @GetMapping("/subject-class")
    public ResponseEntity<?> getAllClassBySubject(@RequestParam String subjectId){
        return classService.getAllClassBySubject(subjectId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Class> getClassById(@PathVariable String id){
        return classService.getClassById(id);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ClassDto>> searchClass(
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String teacherName){
        return classService.searchClass(new ClassQueryParam(subjectName, teacherName));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createClass(@RequestBody ClassDto classDto){
        return classService.createClass(classDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteClass(@PathVariable String id){
        return classService.deleteClass(id);
    }

}
