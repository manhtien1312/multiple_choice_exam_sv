package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.QuestionBankDto;
import com.graduationproject.exam_supervision_server.model.Answer;
import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.repository.QuestionBankRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.QuestionBankService;
import com.graduationproject.exam_supervision_server.utils.dtomapper.QuestionBankMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private QuestionBankMapper questionBankMapper;

    @Override
    public ResponseEntity<QuestionBankDto> getBySubjectId(String id, int type) {
        Optional<QuestionBank> qb = questionBankRepository.findBySubjectId(UUID.fromString(id), type);
        if (qb.isEmpty()){
            throw new RuntimeException("Ngân hàng không tồn tại, kiểm tra lại ID của môn học");
        }
        else {
            qb.get().getQuestions().sort(Comparator.comparing(Question::getQuestionCode));
            return ResponseEntity.status(HttpStatus.OK).body(qb.map(questionBankMapper).get());
        }
    }

    @Override
    public void generateExcel(String subjectId, HttpServletResponse response) throws IOException {
        QuestionBank reviewQB = questionBankRepository.findBySubjectId(UUID.fromString(subjectId), 0).get();
        QuestionBank examQB = questionBankRepository.findBySubjectId(UUID.fromString(subjectId), 1).get();

        XSSFWorkbook workbook = new XSSFWorkbook();

        // Font chữ in đậm và căn giữa ô
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Wrap text
        CellStyle wrapTextStyle = workbook.createCellStyle();
        wrapTextStyle.setWrapText(true);
        wrapTextStyle.setAlignment(HorizontalAlignment.CENTER);
        wrapTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Tạo 2 sheet là 2 ngân hàng câu hỏi
        XSSFSheet reviewSheet = workbook.createSheet("Ngân hàng ôn tập");
        XSSFSheet examSheet = workbook.createSheet("Ngân hàng thi");

        XSSFRow reviewRow = reviewSheet.createRow(0);
        XSSFRow examRow = examSheet.createRow(0);

        // Độ rộng và tiêu đề từng cột
        int[] columnWidthArr = {3000, 3000, 15000, 10000, 10000, 10000, 10000, 3000, 10000};
        String[] headerArr = {"Mã câu hỏi", "Loại câu hỏi", "Nội dung", "Đáp án 1", "Đáp án 2", "Đáp án 3", "Đáp án 4", "Đáp án đúng", "Giải thích"};

        for (int i=0; i<=8; i++){
            reviewSheet.setColumnWidth(i, columnWidthArr[i]);
            examSheet.setColumnWidth(i, columnWidthArr[i]);

            XSSFCell reviewCell = reviewRow.createCell(i);
            reviewCell.setCellValue(headerArr[i]);
            reviewCell.setCellStyle(headerStyle);

            XSSFCell examCell = examRow.createCell(i);
            examCell.setCellValue(headerArr[i]);
            examCell.setCellStyle(headerStyle);
        }

        // Nội dung 2 ngân hàng câu hỏi
        int reviewRowIndex = 1;
        List<Question> sortedQuestions = reviewQB.getQuestions();
        sortedQuestions.sort(Comparator.comparing(Question::getQuestionCode));
        for(Question question : sortedQuestions){
            displayQuestionInExcel(reviewSheet, reviewRowIndex, wrapTextStyle, question);
            reviewRowIndex++;
        }

        int examRowIndex = 1;
        sortedQuestions = examQB.getQuestions();
        sortedQuestions.sort(Comparator.comparing(Question::getQuestionCode));
        for(Question question : examQB.getQuestions()){
            displayQuestionInExcel(examSheet, examRowIndex, wrapTextStyle, question);
            examRowIndex++;
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();

    }

    // Đổ nội dung câu hỏi lên 1 dòng excel
    private void displayQuestionInExcel(XSSFSheet sheet, int rowIndex, CellStyle style, Question question){
        XSSFRow dataRow = sheet.createRow(rowIndex);
        dataRow.createCell(0).setCellValue(question.getQuestionCode());

        XSSFCell typeCell = dataRow.createCell(1);
        typeCell.setCellValue(question.getType().getTypeName());
        typeCell.setCellStyle(style);

        XSSFCell contentCell = dataRow.createCell(2);
        contentCell.setCellValue(question.getQuestionContent());
        contentCell.setCellStyle(style);

        List<Answer> answers = question.getAnswers();
        for (int i=0; i<answers.size(); i++){
            XSSFCell answerCell = dataRow.createCell(i+3);
            answerCell.setCellValue(answers.get(i).getAnswerContent());
            answerCell.setCellStyle(style);
            if(answers.get(i).getIsCorrect()){
                XSSFCell correctAnswerCell = dataRow.createCell(7);
                correctAnswerCell.setCellValue(i+1);
                correctAnswerCell.setCellStyle(style);
            }
        }

        XSSFCell explainCell = dataRow.createCell(8);
        explainCell.setCellValue(question.getExplanation());
        explainCell.setCellStyle(style);
    }
}
