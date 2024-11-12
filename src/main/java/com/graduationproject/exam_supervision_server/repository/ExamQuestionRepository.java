package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, UUID> {

    @Query(value = "SELECT eq FROM ExamQuestion eq WHERE eq.examQuestionCode=:examQuestionCode")
    Optional<ExamQuestion> findByExamQuestionCode(String examQuestionCode);

    @Query(value = "SELECT eq FROM ExamQuestion eq WHERE eq.subject.id=:subjectId AND eq.createdBy.id=:teacherId ORDER BY eq.examQuestionCode")
    List<ExamQuestion> findAllBySubjectId(UUID subjectId, UUID teacherId);

}
