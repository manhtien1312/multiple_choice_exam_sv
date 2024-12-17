package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Major;
import com.graduationproject.exam_supervision_server.service.serviceinterface.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping
    public ResponseEntity<List<Major>> getAllMajors(){
        return majorService.getAllMajors();
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addMajor(@RequestBody Major major){
        return majorService.addMajor(major);
    }

}
