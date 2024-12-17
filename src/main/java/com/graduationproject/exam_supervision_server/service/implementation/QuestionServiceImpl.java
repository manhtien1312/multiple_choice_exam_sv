package com.graduationproject.exam_supervision_server.service.implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private ObjectMapper objectMapper;
    private final Dotenv dotenv = Dotenv.load();
    private final Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

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
    public ResponseEntity<?> filterQuestion(String questionBankId, String typeName, int level, String searchText) {
        try {
            List<Question> questions = questionBankRepository.findById(UUID.fromString(questionBankId)).get().getQuestions();
            questions.sort(Comparator.comparing(Question::getQuestionCode));
            List<Question> filteredTypeQuestions;
            List<Question> filteredLevelQuestions;
            List<Question> res;

            // Lọc loại câu hỏi
            if(!typeName.isBlank()){
                filteredTypeQuestions = questions.stream()
                        .filter(question -> question.getType().getTypeName().equals(typeName))
                        .toList();
            }
            else {
                filteredTypeQuestions = questions;
            }

            // Lọc độ khó
            if(level != 0){
                filteredLevelQuestions = filteredTypeQuestions.stream()
                        .filter(question -> question.getLevel() == level)
                        .toList();
            }
            else {
                filteredLevelQuestions = filteredTypeQuestions;
            }

            // Tìm kiếm
            if(!searchText.isBlank()){
                res = questions.stream()
                        .filter(question -> question.getQuestionContent().toLowerCase().contains(searchText.toLowerCase()))
                        .toList();
            } else {
                res = filteredLevelQuestions;
            }

            return ResponseEntity.status(HttpStatus.OK).body(res);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> addQuestion(String questionBankId, String questionStr, MultipartFile questionImage) {
        try {
            Optional<QuestionBank> qb = questionBankRepository.findById(UUID.fromString(questionBankId));
            Question question = objectMapper.readValue(questionStr, Question.class);

            question.setQuestionBank(qb.get());
            String questionType = question.getType().getTypeName();
            if(questionTypeRepository.existByTypeNameAndSubject(questionType, qb.get().getSubject().getId())){
                question.setType(questionTypeRepository.findByTypeName(questionType, qb.get().getSubject().getId()).get());
            }
            else {
                QuestionType type = question.getType();
                type.setSubject(qb.get().getSubject());
                QuestionType savedType = questionTypeRepository.save(type);
                question.setType(savedType);
            }

            // Lưu hình ảnh của câu hỏi lên cloud và trả về url
            if(questionImage != null){
                Map param = ObjectUtils.asMap(
                        "folder", "question_image",
                        "public_id", question.getQuestionCode()
                );
                Map res = cloudinary.uploader().upload(questionImage.getBytes(), param);
                String imageUrl = (String) res.get("secure_url");
                question.setImageUrl(imageUrl);
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
            Map<String, XSSFPictureData> reviewImageMap = mapImagesToCells(reviewSheet);
            for (int i=1; i<reviewSheet.getPhysicalNumberOfRows(); i++){
                XSSFRow row = reviewSheet.getRow(i);

                // Xác định loại câu hỏi đã có hay chưa
                String typeName = row.getCell(1).getStringCellValue().trim();
                QuestionType type;
                if(questionTypeRepository.existByTypeNameAndSubject(typeName, reviewQB.getSubject().getId())){
                    type = questionTypeRepository.findByTypeName(typeName, reviewQB.getSubject().getId()).get();
                }
                else {
                    var newType = QuestionType.builder()
                            .typeName(typeName)
                            .subject(reviewQB.getSubject())
                            .build();
                    type = questionTypeRepository.save(newType);
                }

                // Xác định câu hỏi đã tồn tại hay chưa, nếu rồi thì cập nhật, nếu chưa thì thêm mới
                StringBuilder sb = new StringBuilder();
                sb.append("0.");
                sb.append(row.getCell(0).getStringCellValue().trim());
                String questionCode = sb.toString();
                Optional<Question> questionOptional = questionRepository.findByQuestionCode(questionCode, reviewQB.getId());

                // Nếu là câu hỏi mới
                if(questionOptional.isEmpty()){
                    // Kiểm tra cột chứa hình ảnh
                    XSSFCell imageCell = row.getCell(3);
                    String imageUrl = "";
                    if(imageCell != null){
                        String cellRef = imageCell.getAddress().formatAsString();
                        if(reviewImageMap.containsKey(cellRef)){
                            Map param = ObjectUtils.asMap(
                                    "folder", "question_image",
                                    "public_id", questionCode
                            );
                            Map res = cloudinary.uploader().upload(reviewImageMap.get(cellRef).getData(), param);
                            imageUrl = (String) res.get("secure_url");
                        }
                    }
                    var question = Question.builder()
                            .type(type)
                            .questionCode(questionCode)
                            .questionContent(row.getCell(2).getStringCellValue().trim())
                            .imageUrl(imageUrl)
                            .questionBank(reviewQB)
                            .level((int) row.getCell(9).getNumericCellValue())
                            .explanation(row.getCell(10) == null ? "" : row.getCell(10).getStringCellValue().trim())
                            .build();
                    Question savedQuestion = questionRepository.save(question);

                    for (int j=4; j<=7; j++){
                        var answer = Answer.builder()
                                .question(savedQuestion)
                                .answerContent(row.getCell(j).getStringCellValue().trim())
                                .isCorrect((int) row.getCell(8).getNumericCellValue() + 3 == j)
                                .build();
                        answers.add(answer);
                    }
                }
                // Nếu câu hỏi đã tồn tại
                else {
                    // Kiểm tra cột chứa hình ảnh
                    XSSFCell imageCell = row.getCell(3);
                    String imageUrl = "";
                    if(imageCell != null){
                        String cellRef = imageCell.getAddress().formatAsString();
                        if(reviewImageMap.containsKey(cellRef)){
                            Map param = ObjectUtils.asMap(
                                    "folder", "question_image",
                                    "public_id", questionCode
                            );
                            Map res = cloudinary.uploader().upload(reviewImageMap.get(cellRef).getData(), param);
                            imageUrl = (String) res.get("secure_url");
                        }
                    }
                    Question question = questionOptional.get();
                    question.setType(type);
                    question.setQuestionContent(row.getCell(2).getStringCellValue().trim());
                    question.setImageUrl(imageUrl);
                    question.setLevel((int) row.getCell(9).getNumericCellValue());
                    question.setExplanation(row.getCell(10).getStringCellValue().trim());

                    List<Answer> savedAnswers = question.getAnswers();
                    for (int j=0; j<savedAnswers.size(); j++){
                        savedAnswers.get(j).setAnswerContent(row.getCell(j+4).getStringCellValue().trim());
                        savedAnswers.get(j).setIsCorrect((int) row.getCell(8).getNumericCellValue() == j+1);
                    }
                    question.setAnswers(savedAnswers);

                    questionRepository.save(question);
                }
            }

            XSSFSheet examSheet = workbook.getSheetAt(1);
            Map<String, XSSFPictureData> examImageMap = mapImagesToCells(examSheet);
            for (int i=1; i<examSheet.getPhysicalNumberOfRows(); i++){
                XSSFRow row = examSheet.getRow(i);

                // Xác định loại câu hỏi đã có hay chưa
                String typeName = row.getCell(1).getStringCellValue().trim();
                QuestionType type;
                if(questionTypeRepository.existByTypeNameAndSubject(typeName, examQB.getSubject().getId())){
                    type = questionTypeRepository.findByTypeName(typeName, examQB.getSubject().getId()).get();
                }
                else {
                    var newType = QuestionType.builder()
                            .typeName(typeName)
                            .subject(examQB.getSubject())
                            .build();
                    type = questionTypeRepository.save(newType);
                }

                // Xác định câu hỏi đã tồn tại hay chưa, nếu rồi thì cập nhật, nếu chưa thì thêm mới
                StringBuilder sb = new StringBuilder();
                sb.append("1.");
                sb.append(row.getCell(0).getStringCellValue().trim());
                String questionCode = sb.toString();
                Optional<Question> questionOptional = questionRepository.findByQuestionCode(questionCode, examQB.getId());

                // Nếu là câu hỏi mới
                if(questionOptional.isEmpty()){
                    // Kiểm tra cột chứa hình ảnh
                    XSSFCell imageCell = row.getCell(3);
                    String imageUrl = null;
                    if(imageCell != null){
                        String cellRef = imageCell.getAddress().formatAsString();
                        if(examImageMap.containsKey(cellRef)){
                            Map param = ObjectUtils.asMap(
                                    "folder", "question_image",
                                    "public_id", questionCode
                            );
                            Map res = cloudinary.uploader().upload(examImageMap.get(cellRef).getData(), param);
                            imageUrl = (String) res.get("secure_url");
                        }
                    }
                    var question = Question.builder()
                            .type(type)
                            .questionCode(questionCode)
                            .questionContent(row.getCell(2).getStringCellValue().trim())
                            .imageUrl(imageUrl)
                            .questionBank(examQB)
                            .level((int) row.getCell(9).getNumericCellValue())
                            .explanation(row.getCell(10) == null ? "" : row.getCell(10).getStringCellValue().trim())
                            .build();
                    Question savedQuestion = questionRepository.save(question);

                    for (int j=4; j<=7; j++){
                        var answer = Answer.builder()
                                .question(savedQuestion)
                                .answerContent(row.getCell(j).getStringCellValue().trim())
                                .isCorrect((int) row.getCell(8).getNumericCellValue() + 3 == j)
                                .build();
                        answers.add(answer);
                    }
                }
                // Nếu câu hỏi đã tồn tại
                else {
                    // Kiểm tra cột chứa hình ảnh
                    XSSFCell imageCell = row.getCell(3);
                    String imageUrl = null;
                    if(imageCell != null){
                        String cellRef = imageCell.getAddress().formatAsString();
                        if(examImageMap.containsKey(cellRef)){
                            Map param = ObjectUtils.asMap(
                                    "folder", "question_image",
                                    "public_id", questionCode
                            );
                            Map res = cloudinary.uploader().upload(examImageMap.get(cellRef).getData(), param);
                            imageUrl = (String) res.get("secure_url");
                        }
                    }
                    Question question = questionOptional.get();
                    question.setType(type);
                    question.setQuestionContent(row.getCell(2).getStringCellValue().trim());
                    question.setImageUrl(imageUrl);
                    question.setLevel((int) row.getCell(9).getNumericCellValue());
                    question.setExplanation(row.getCell(10).getStringCellValue().trim());

                    List<Answer> savedAnswers = question.getAnswers();
                    for (int j=0; j<savedAnswers.size(); j++){
                        savedAnswers.get(j).setAnswerContent(row.getCell(j+4).getStringCellValue().trim());
                        savedAnswers.get(j).setIsCorrect((int) row.getCell(8).getNumericCellValue() == j+1);
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
    public ResponseEntity<MessageResponse> modifyQuestion(String id, String questionStr, MultipartFile questionImage) {
        try {
            Question question = objectMapper.readValue(questionStr, Question.class);
            Question savedQuestion = questionRepository.findById(UUID.fromString(id)).get();

            // Lưu hình ảnh của câu hỏi lên cloud và trả về url
            if(questionImage != null){
                Map param = ObjectUtils.asMap(
                        "folder", "question_image",
                        "public_id", question.getQuestionCode()
                );
                Map res = cloudinary.uploader().upload(questionImage.getBytes(), param);
                String imageUrl = (String) res.get("secure_url");
                savedQuestion.setImageUrl(imageUrl);
            }

//            savedQuestion.setType(questionTypeRepository.findByTypeName(question.getType().getTypeName(), ).get());
            savedQuestion.setQuestionContent(question.getQuestionContent());
            savedQuestion.setLevel(question.getLevel());
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

    private Map<String, XSSFPictureData> mapImagesToCells(Sheet sheet){
        Map<String, XSSFPictureData> imageMap = new HashMap<>();
        if (sheet instanceof XSSFSheet) {
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            for (POIXMLDocumentPart part : xssfSheet.getRelations()) {
                if (part instanceof XSSFDrawing) {
                    XSSFDrawing drawing = (XSSFDrawing) part;
                    for (XSSFShape shape : drawing.getShapes()) {
                        if (shape instanceof XSSFPicture) {
                            XSSFPicture picture = (XSSFPicture) shape;
                            ClientAnchor anchor = picture.getPreferredSize();
                            int row = anchor.getRow1();
                            int col = anchor.getCol1();
                            String cellRef = new CellAddress(row, col).formatAsString();
                            imageMap.put(cellRef, picture.getPictureData());
                        }
                    }
                }
            }
        }
        return imageMap;
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
