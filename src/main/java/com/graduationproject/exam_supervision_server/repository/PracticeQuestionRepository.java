package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.PracticeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeQuestionRepository extends JpaRepository<PracticeQuestion, Long> {
    List<PracticeQuestion> findBySubjectId(Long subjectId);
}
