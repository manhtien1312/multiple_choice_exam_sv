package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.ClassDto;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Class;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ClassService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<ClassDto>> searchClass(@RequestParam String searchText){
        return classService.searchClass(searchText);
    }

    @GetMapping("/export-class-students")
    public void exportClassStudents(@RequestParam String classId, HttpServletResponse response) throws Exception{
        Class classDto = classService.getClassById(classId).getBody();
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=danh-sach-sinh-vien-" + normalizeString(classDto.getClassName() + " " + classDto.getSubject().getSubjectName()) + ".xlsx";
        response.setHeader(headerKey, headerValue);

        classService.generateListStudentExcel(classId, response);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createClass(@RequestParam String subjectName, @RequestParam MultipartFile classFile) throws IOException {
        return classService.createClass(subjectName, classFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteClass(@PathVariable String id){
        return classService.deleteClass(id);
    }

    // Hàm chuẩn hóa tên file, vd: Lập trình mạng -> lap-trinh-mang
    private String normalizeString(String className){
        // chuẩn hóa ký tự
        String normalized = Normalizer.normalize(className, Normalizer.Form.NFD);

        // loại bỏ ký tự dấu
        String noAccentsStr = normalized.replaceAll("\\p{M}", "");

        // thay thế khoảng trắng bằng gạch ngang
        String result = noAccentsStr.replaceAll(" ", "-").toLowerCase();

        // loại bỏ ký tự đặc biệt
        result = result.replaceAll("[^a-zA-Z0-9-]", "");
        return result;
    }

}
