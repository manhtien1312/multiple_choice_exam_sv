package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.ClassStatisticsDTO;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ClassStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private ClassStatisticsService classStatisticsService;

    @GetMapping("/class/{classId}")
    public ResponseEntity<ClassStatisticsDTO> getClassStatistics(@PathVariable String classId) {
        try {
            UUID classUUID = UUID.fromString(classId);
            ClassStatisticsDTO statistics = classStatisticsService.getClassStatistics(classUUID);
            return ResponseEntity.ok(statistics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
