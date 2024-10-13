package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Subject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    @Transactional
    @Query(value = "SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Subject s WHERE s.subjectCode=:subjectCode")
    boolean existBySubjectCode(String subjectCode);

}
