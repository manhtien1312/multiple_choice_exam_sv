package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, UUID> {
    List<ExamResult> findByExamId(UUID examId);
}
