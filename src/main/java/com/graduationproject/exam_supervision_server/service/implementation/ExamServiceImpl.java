package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.request.CreateExamRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Exam;
import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import com.graduationproject.exam_supervision_server.model.ExamStudent;
import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.repository.ExamRepository;
import com.graduationproject.exam_supervision_server.repository.ExamStudentRepository;
import com.graduationproject.exam_supervision_server.repository.StudentRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ExamService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamStudentRepository examStudentRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public ResponseEntity<?> getExamByClassAndTeacher(String classId) {
        try {
            List<Exam> res = examRepository.findAllByClassId(UUID.fromString(classId));
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Vui lòng thử lại sau."));
        }
    }

    @Override
    public ResponseEntity<?> getExamByClassAndStudent(String classId) {
        try {
            List<ExamStudent> examStudents = examStudentRepository.findAllByClassIdAndStudentId(UUID.fromString(classId), getStudentFromRequest().getId());
            List<Exam> res = new ArrayList<>();
            for(ExamStudent es : examStudents){
                res.add(es.getExam());
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Vui lòng thử lại sau."));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> createExam(CreateExamRequest request) {
        try {
            // Lưu thông tin của bài thi
            Exam savedExam = examRepository.save(request.exam());

            // Nếu nhiều hơn 1 đề thi được chọn, thực hiện phân chia đề thi cho sinh viên
            List<ExamStudent> examStudents = new ArrayList<>();
            if(request.selectedExamQuestions().size() == 1){
                for (Student student : request.allowedStudents()){
                    ExamStudent es = ExamStudent.builder()
                            .exam(savedExam)
                            .student(student)
                            .examQuestion(request.selectedExamQuestions().get(0))
                            .build();
                    examStudents.add(es);
                }
            } else {
                for (int i = 0; i < request.allowedStudents().size(); i++) {
                    ExamStudent es = ExamStudent.builder()
                            .exam(savedExam)
                            .student(request.allowedStudents().get(i))
                            .examQuestion(request.selectedExamQuestions().get(i % request.selectedExamQuestions().size()))
                            .build();
                    examStudents.add(es);
                }
            }

            examStudentRepository.saveAll(examStudents);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Tạo bài thi thành công!"));

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Vui lòng thử lại sau."));
        }
    }

    private Student getStudentFromRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return studentRepository.findByAccountUsername(userDetails.getUsername()).orElseThrow();
    }
}
