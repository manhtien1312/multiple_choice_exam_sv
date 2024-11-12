package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    // Lấy câu hỏi theo QuestionBankId
    List<Question> findByQuestionBankId(UUID questionBankId);

    // Lấy câu hỏi theo subjectId và type của QuestionBank
    List<Question> findByQuestionBank_SubjectIdAndQuestionBank_Type(String subjectId, String type);
}
