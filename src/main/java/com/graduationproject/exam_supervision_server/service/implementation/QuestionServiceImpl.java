package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Answer;
import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.repository.AnswerRepository;
import com.graduationproject.exam_supervision_server.repository.QuestionBankRepository;
import com.graduationproject.exam_supervision_server.repository.QuestionRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.QuestionService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService  {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public ResponseEntity<?> getQuestionById(String id) {
        try {
            Question question = questionRepository.findById(UUID.fromString(id)).get();
            return ResponseEntity.status(HttpStatus.OK).body(question);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> addQuestion(String questionBankId, Question question) {
        try {
            Optional<QuestionBank> qb = questionBankRepository.findById(UUID.fromString(questionBankId));
            question.setQuestionBank(qb.get());
            Question savedQuestion = questionRepository.save(question);

            List<Answer> answers = question.getAnswers();
            for(Answer answer : answers){
                answer.setQuestion(savedQuestion);
            }
            answerRepository.saveAll(answers);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm câu hỏi thành công"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addThroughFile(String questionBankId, MultipartFile questionFile) {
        try {
            QuestionBank qb = questionBankRepository.findById(UUID.fromString(questionBankId)).get();
            List<Answer> answers = new ArrayList<>();

            XSSFWorkbook workbook = new XSSFWorkbook(questionFile.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++){
                XSSFRow row = sheet.getRow(i);

                var question = Question.builder()
                        .type(row.getCell(0).getStringCellValue().trim().equals("Lý thuyết") ? 1 : 2)
                        .questionContent(row.getCell(1).getStringCellValue().trim())
                        .questionBank(qb)
                        .explanation(row.getCell(7).getStringCellValue().trim())
                        .build();
                Question savedQuestion = questionRepository.save(question);

                for (int j=2; j<=5; j++){
                    var answer = Answer.builder()
                            .question(savedQuestion)
                            .answerContent(row.getCell(j).getStringCellValue().trim())
                            .isCorrect((int) row.getCell(6).getNumericCellValue() + 1 == j)
                            .build();
                    answers.add(answer);
                }
            }

            answerRepository.saveAll(answers);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Thêm danh sách câu hỏi thành công"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> modifyQuestion(String id, Question question) {
        try {
            Question savedQuestion = questionRepository.findById(UUID.fromString(id)).get();
            savedQuestion.setType(question.getType());
            savedQuestion.setQuestionContent(question.getQuestionContent());
            savedQuestion.setExplanation(question.getExplanation());

            List<Answer> savedAnswers = savedQuestion.getAnswers();
            List<Answer> newAnswers = question.getAnswers();
            for (int i=0; i<savedAnswers.size(); i++){
                savedAnswers.get(i).setAnswerContent(newAnswers.get(i).getAnswerContent());
                savedAnswers.get(i).setIsCorrect(newAnswers.get(i).getIsCorrect());
            }

            savedQuestion.setAnswers(savedAnswers);
            questionRepository.save(savedQuestion);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Cập nhật câu hỏi thành công"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteQuestion(String id) {
        try {
            questionRepository.deleteById(UUID.fromString(id));
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Xóa câu hỏi thành công"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }
}
