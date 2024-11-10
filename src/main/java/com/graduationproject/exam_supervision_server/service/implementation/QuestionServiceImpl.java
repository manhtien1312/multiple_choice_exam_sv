package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Answer;
import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.model.QuestionType;
import com.graduationproject.exam_supervision_server.repository.AnswerRepository;
import com.graduationproject.exam_supervision_server.repository.QuestionBankRepository;
import com.graduationproject.exam_supervision_server.repository.QuestionRepository;
import com.graduationproject.exam_supervision_server.repository.QuestionTypeRepository;
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
    private QuestionTypeRepository questionTypeRepository;
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
            String questionType = question.getType().getTypeName();
            if(questionTypeRepository.existByTypeName(questionType)){
                question.setType(questionTypeRepository.findByTypeName(questionType).get());
            }
            else {
                QuestionType savedType = questionTypeRepository.save(question.getType());
                question.setType(savedType);
            }
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

    @Transactional
    @Override
    public ResponseEntity<MessageResponse> addThroughFile(String subjectId, MultipartFile questionFile) {
        try {
            QuestionBank reviewQB = questionBankRepository.findBySubjectId(UUID.fromString(subjectId), 0).get();
            QuestionBank examQB = questionBankRepository.findBySubjectId(UUID.fromString(subjectId), 1).get();

            List<Answer> answers = new ArrayList<>();

            XSSFWorkbook workbook = new XSSFWorkbook(questionFile.getInputStream());
            XSSFSheet reviewSheet = workbook.getSheetAt(0);
            for (int i=1; i<reviewSheet.getPhysicalNumberOfRows(); i++){
                XSSFRow row = reviewSheet.getRow(i);

                // Xác định loại câu hỏi đã có hay chưa
                String typeName = row.getCell(1).getStringCellValue().trim();
                QuestionType type;
                if(questionTypeRepository.existByTypeName(typeName)){
                    type = questionTypeRepository.findByTypeName(typeName).get();
                }
                else {
                    var newType = QuestionType.builder()
                            .typeName(typeName)
                            .build();
                    type = questionTypeRepository.save(newType);
                }

                // Xác định câu hỏi đã tồn tại hay chưa, nếu rồi thì cập nhật, nếu chưa thì thêm mới
                String questionCode = row.getCell(0).getStringCellValue().trim();
                Optional<Question> questionOptional = questionRepository.findByQuestionCode(questionCode);

                // Nếu là câu hỏi mới
                if(questionOptional.isEmpty()){
                    var question = Question.builder()
                            .type(type)
                            .questionCode(questionCode)
                            .questionContent(row.getCell(2).getStringCellValue().trim())
                            .questionBank(reviewQB)
                            .explanation(row.getCell(8) == null ? "" : row.getCell(8).getStringCellValue().trim())
                            .build();
                    Question savedQuestion = questionRepository.save(question);

                    for (int j=3; j<=6; j++){
                        var answer = Answer.builder()
                                .question(savedQuestion)
                                .answerContent(row.getCell(j).getStringCellValue().trim())
                                .isCorrect((int) row.getCell(7).getNumericCellValue() + 2 == j)
                                .build();
                        answers.add(answer);
                    }
                }
                // Nếu câu hỏi đã tồn tại
                else {
                    Question question = questionOptional.get();
                                        question.setType(type);
                    question.setQuestionContent(row.getCell(2).getStringCellValue().trim());
                    question.setExplanation(row.getCell(8).getStringCellValue().trim());

                    List<Answer> savedAnswers = question.getAnswers();
                    for (int j=0; j<savedAnswers.size(); j++){
                        savedAnswers.get(j).setAnswerContent(row.getCell(j+3).getStringCellValue().trim());
                        savedAnswers.get(j).setIsCorrect((int) row.getCell(7).getNumericCellValue() == j+1);
                    }
                    question.setAnswers(savedAnswers);

                    questionRepository.save(question);
                }
            }

            XSSFSheet examSheet = workbook.getSheetAt(1);
            for (int i=1; i<examSheet.getPhysicalNumberOfRows(); i++){
                XSSFRow row = examSheet.getRow(i);

                // Xác định loại câu hỏi đã có hay chưa
                String typeName = row.getCell(1).getStringCellValue().trim();
                QuestionType type;
                if(questionTypeRepository.existByTypeName(typeName)){
                    type = questionTypeRepository.findByTypeName(typeName).get();
                }
                else {
                    var newType = QuestionType.builder()
                            .typeName(typeName)
                            .build();
                    type = questionTypeRepository.save(newType);
                }

                // Xác định câu hỏi đã tồn tại hay chưa, nếu rồi thì cập nhật, nếu chưa thì thêm mới
                String questionCode = row.getCell(0).getStringCellValue().trim();
                Optional<Question> questionOptional = questionRepository.findByQuestionCode(questionCode);

                // Nếu là câu hỏi mới
                if(questionOptional.isEmpty()){
                    var question = Question.builder()
                            .type(type)
                            .questionCode(questionCode)
                            .questionContent(row.getCell(2).getStringCellValue().trim())
                            .questionBank(examQB)
                            .explanation(row.getCell(8) == null ? "" : row.getCell(8).getStringCellValue().trim())
                            .build();
                    Question savedQuestion = questionRepository.save(question);

                    for (int j=3; j<=6; j++){
                        var answer = Answer.builder()
                                .question(savedQuestion)
                                .answerContent(row.getCell(j).getStringCellValue().trim())
                                .isCorrect((int) row.getCell(7).getNumericCellValue() + 2 == j)
                                .build();
                        answers.add(answer);
                    }
                }
                // Nếu câu hỏi đã tồn tại
                else {
                    Question question = questionOptional.get();
                    question.setType(type);
                    question.setQuestionContent(row.getCell(2).getStringCellValue().trim());
                    question.setExplanation(row.getCell(8).getStringCellValue().trim());

                    List<Answer> savedAnswers = question.getAnswers();
                    for (int j=0; j<savedAnswers.size(); j++){
                        savedAnswers.get(j).setAnswerContent(row.getCell(j+3).getStringCellValue().trim());
                        savedAnswers.get(j).setIsCorrect((int) row.getCell(7).getNumericCellValue() == j+1);
                    }
                    question.setAnswers(savedAnswers);

                    questionRepository.save(question);
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
            savedQuestion.setType(questionTypeRepository.findByTypeName(question.getType().getTypeName()).get());
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





    // NV Ngọc
    @Override
    public List<Question> getPracticeQuestionsBySubject(String subjectId) {
//        return questionRepository.findBySubjectId(UUID.fromString(subjectId));
        return null;
    }

    @Override
    public boolean checkAnswer(String questionId, String selectedAnswer) {
//        Question question = questionRepository.findById(UUID.fromString(questionId)).orElseThrow();
        return false;
    }

    @Override
    public String getExplanation(String questionId) {
//        Question question = questionRepository.findById(UUID.fromString(questionId)).orElseThrow();
        return null;
    }
}
