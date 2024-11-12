package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.ExamResultDTO;
import com.graduationproject.exam_supervision_server.utils.dtomapper.ExamResultMapper;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExamResultServiceImpl implements ExamResultServiceInterface {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private ExamResultMapper examResultMapper;

    @Override
    public List<ExamResultDTO> getResultsByExam(UUID examId) {
        // Tìm kết quả bài thi từ repository
        List<ExamResult> results = examResultRepository.findByExamId(examId);
        // Chuyển các kết quả thành DTO
        return results.stream().map(examResultMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void exportResultsToExcel(UUID examId) throws IOException {
        // Lấy kết quả bài thi theo examId
        List<ExamResultDTO> results = getResultsByExam(examId);

        // Tạo một workbook và một sheet mới
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Exam Results");

        // Tạo header cho bảng Excel
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Student Name", "Correct Answers", "Wrong Answers", "Unanswered Questions", "Score"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Tạo các dòng dữ liệu cho kết quả
        int rowNum = 1;
        for (ExamResultDTO result : results) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(result.getStudentName());  // Đảm bảo ExamResultDTO có trường studentName
            row.createCell(1).setCellValue(result.getCorrectQuestions());
            row.createCell(2).setCellValue(result.getWrongQuestions());
            row.createCell(3).setCellValue(result.getBlankQuestions());
            row.createCell(4).setCellValue(result.getScore());
        }

        // Tạo đường dẫn tệp Excel dựa trên examId
        String filePath = "ExamResults_" + examId.toString() + ".xlsx";

        // Ghi dữ liệu vào tệp Excel
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
