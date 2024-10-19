package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.QuestionBank;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, UUID> {

    @Transactional
    @Query(value = "SELECT qb FROM QuestionBank qb " +
            "WHERE qb.subject.id=:subjectId AND qb.type=:type")
    Optional<QuestionBank> findBySubjectId(UUID subjectId, int type);

}
