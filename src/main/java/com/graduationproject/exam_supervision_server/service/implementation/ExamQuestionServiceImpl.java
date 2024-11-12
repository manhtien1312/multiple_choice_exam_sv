package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.request.ChangeQuestionRequest;
import com.graduationproject.exam_supervision_server.dto.request.CreateExamQuestionRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.model.Teacher;
import com.graduationproject.exam_supervision_server.repository.*;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ExamQuestionService;
import com.graduationproject.exam_supervision_server.utils.ExamQuestionDetail;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamQuestionServiceImpl implements ExamQuestionService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private ExamQuestionRepository examQuestionRepository;
    @Autowired
    private QuestionTypeRepository questionTypeRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional
    @Override
    public ResponseEntity<MessageResponse> createExamQuestion(CreateExamQuestionRequest request) {
        try {
            QuestionBank qb = questionBankRepository.findBySubjectId(UUID.fromString(request.subjectId()), 1).get();
            List<Question> questions = new ArrayList<>();
            for (ExamQuestionDetail detail : request.details()){
                if(detail.numberOfQuestions() == 0){
                    continue;
                }
                Pageable limit = PageRequest.of(0, detail.numberOfQuestions());
                List<Question> randomQuestions = questionRepository.findRandomQuestionsByType(detail.typeName(), qb.getId(), limit);
                questions.addAll(randomQuestions);
            }

            var examQuestion = ExamQuestion.builder()
                    .examQuestionCode(request.examQuestionCode())
                    .totalQuestions(request.totalQuestions())
                    .createdBy(getTeacherFromRequest())
                    .subject(subjectRepository.findById(UUID.fromString(request.subjectId())).get())
                    .questions(questions)
                    .build();

            examQuestionRepository.save(examQuestion);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Tạo đề thi thành công"));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<?> getAllExamQuestionBySubject(String subjectId) {
        try {
            List<ExamQuestion> res = examQuestionRepository.findAllBySubjectId(UUID.fromString(subjectId), getTeacherFromRequest().getId());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<?> getExamQuestionById(String examQuestionId) {
        try {
            Optional<ExamQuestion> examQuestion = examQuestionRepository.findById(UUID.fromString(examQuestionId));
            if(examQuestion.isPresent()){
                ExamQuestion res = examQuestion.get();
                res.getQuestions().sort(Comparator.comparing(Question::getQuestionCode));
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Không tìm thấy đề thi, kiểm tra lại mã đề"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> changeQuestion(ChangeQuestionRequest request) {
        try {
            ExamQuestion examQuestion = examQuestionRepository.findById(UUID.fromString(request.examQuestionId())).get();
            List<Question> questions = examQuestion.getQuestions();
            questions.add(request.newQuestion());
            List<Question> newQuestions = questions.stream()
                            .filter(question -> !question.getId().equals(request.oldQuestion().getId()))
                            .collect(Collectors.toList());
            examQuestion.setQuestions(newQuestions);
            examQuestionRepository.save(examQuestion);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Thay đổi câu hỏi thành công"));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteExamQuestion(String id) {
        try {
            examQuestionRepository.deleteById(UUID.fromString(id));
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Xóa đề thi thành công"));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    private Teacher getTeacherFromRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return teacherRepository.findByAccountUsername(userDetails.getUsername()).orElseThrow();
    }
}
