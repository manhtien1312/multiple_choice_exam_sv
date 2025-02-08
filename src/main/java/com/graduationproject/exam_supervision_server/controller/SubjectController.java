package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.SubjectDto;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Subject;
import com.graduationproject.exam_supervision_server.service.serviceinterface.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<?> getAllSubjects(){
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubjectById(@PathVariable String id){
        return subjectService.getSubjectById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSubject(@RequestParam String searchText){
        return subjectService.searchSubject(searchText);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterSubject(@RequestParam String majorName){
        return subjectService.filterSubject(majorName);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addSubject(@Valid @RequestBody SubjectDto subjectDto){
        return subjectService.addSubject(subjectDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> modifySubject(@PathVariable String id, @Valid @RequestBody SubjectDto subjectDto){
        return subjectService.modifySubject(id, subjectDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteSubject(@PathVariable String id){
        return subjectService.deleteSubject(id);
    }

}
