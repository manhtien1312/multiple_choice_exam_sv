package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.model.ExamResult;
import com.graduationproject.exam_supervision_server.repository.ExamResultRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ExamResultServiceInterface;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExamResultServiceImpl implements ExamResultServiceInterface {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Override
    public List<ExamResult> getResultsByExam(Long examId) {
        return examResultRepository.findByExamId(examId);
    }

    @Override
    public void exportResultsToExcel(Long examId) throws IOException {
        List<ExamResult> results = getResultsByExam(examId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Exam Results");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Student ID");
        headerRow.createCell(1).setCellValue("Correct Answers");
        headerRow.createCell(2).setCellValue("Wrong Answers");
        headerRow.createCell(3).setCellValue("Unanswered Questions");
        headerRow.createCell(4).setCellValue("Score");

        int rowNum = 1;
        for (ExamResult result : results) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(result.getStudent().getId());
            row.createCell(1).setCellValue(result.getCorrectQuestions());
            row.createCell(2).setCellValue(result.getWrongQuestions());
            row.createCell(3).setCellValue(result.getUnansweredQuestions());
            row.createCell(4).setCellValue(result.getScore());
        }

        FileOutputStream fileOut = new FileOutputStream("exam_results.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}
