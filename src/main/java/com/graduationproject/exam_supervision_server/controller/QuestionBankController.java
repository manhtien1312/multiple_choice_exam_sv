package com.graduationproject.exam_supervision_server.controller;

import com.graduationproject.exam_supervision_server.dto.QuestionBankDto;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.model.Subject;
import com.graduationproject.exam_supervision_server.repository.SubjectRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.QuestionBankService;
import com.graduationproject.exam_supervision_server.service.serviceinterface.SubjectService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/question-bank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public ResponseEntity<QuestionBankDto> getQuestionBankBySubjectId(@RequestParam String subjectId, @RequestParam int type){
        return questionBankService.getBySubjectId(subjectId, type);
    }

    @GetMapping("/export-excel")
    public void exportExcel(@RequestParam String subjectId, HttpServletResponse response) throws Exception{
        String subjectName = subjectRepository.findById(UUID.fromString(subjectId)).get().getSubjectName();

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=ngan-hang-" + normalizeString(subjectName) + ".xlsx";
        response.setHeader(headerKey, headerValue);

        questionBankService.generateExcel(subjectId, response);
    }

    // Hàm chuẩn hóa tên file, vd: Lập trình mạng -> lap-trinh-mang
    private String normalizeString(String subjectName){
        // chuẩn hóa ký tự
        String normalized = Normalizer.normalize(subjectName, Normalizer.Form.NFD);

        // loại bỏ ký tự dấu
        String noAccentsStr = normalized.replaceAll("\\p{M}", "");

        // thay thế khoảng trắng bằng gạch ngang
        String result = noAccentsStr.replaceAll(" ", "-").toLowerCase();

        // loại bỏ ký tự đặc biệt
        result = result.replaceAll("[^a-zA-Z0-9-]", "");
        return result;
    }

}
