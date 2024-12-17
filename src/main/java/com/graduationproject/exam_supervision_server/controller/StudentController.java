package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.service.serviceinterface.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<Page<Student>> getAllStudents(@RequestParam Integer pageNumber){
        return studentService.getAllStudents(pageNumber);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Student>> filterStudent(
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String majorName,
            @RequestParam(required = false) String cohort,
            @RequestParam Integer pageNumber
    ){
        return studentService.filterStudent(searchText, majorName, cohort, pageNumber);
    }

    @GetMapping("/cohort")
    public ResponseEntity<List<String>> getAllCohort(){
        return studentService.getAllCohort();
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addNewStudent(@RequestBody Student student){
        return studentService.addNewStudent(student);
    }

    @PostMapping("/add-to-class")
    public ResponseEntity<MessageResponse> addStudent(@RequestParam String classId, @RequestBody Student student){
        return studentService.addStudentToClass(classId, student);
    }

    @PostMapping("/add-student-file")
    public ResponseEntity<MessageResponse> addStudentFile(@RequestParam MultipartFile studentFile) throws IOException {
        return studentService.addStudentByFile(studentFile);
    }

    @PostMapping("/add-student-file-to-class")
    public ResponseEntity<MessageResponse> addStudentByFile(@RequestParam String classId, @RequestParam MultipartFile studentFile) throws IOException {
        return studentService.addStudentToClassByFile(classId, studentFile);
    }

    @PutMapping
    public ResponseEntity<MessageResponse> updateStudent(@RequestBody Student student){
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/remove-from-class")
    public ResponseEntity<MessageResponse> removeStudentFromClass(@RequestParam String classId, @RequestBody List<String> selectedStudents){
        return studentService.removeStudentFromClass(classId, selectedStudents);
    }

    @DeleteMapping("/delete-student")
    public ResponseEntity<MessageResponse> deleteStudent(@RequestBody List<String> selectedStudents){
        return studentService.deleteStudent(selectedStudents);
    }
}
