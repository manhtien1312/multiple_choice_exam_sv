package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.request.CreateExamRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Exam;
import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import com.graduationproject.exam_supervision_server.model.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExamService {

    ResponseEntity<?> getExamByClassAndTeacher(String classId);
    ResponseEntity<?> getExamByClassAndStudent(String classId);
    ResponseEntity<MessageResponse> createExam(CreateExamRequest request);


}
