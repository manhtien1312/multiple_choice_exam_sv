package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, String> {

}
