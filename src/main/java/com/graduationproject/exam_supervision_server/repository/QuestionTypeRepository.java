package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.QuestionType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType, UUID> {

    @Transactional
    @Query(value = "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM QuestionType t WHERE t.typeName=:typeName")
    boolean existByTypeName(String typeName);

    @Query(value = "SELECT t FROM QuestionType t WHERE t.typeName=:typeName")
    Optional<QuestionType> findByTypeName(String typeName);

}
