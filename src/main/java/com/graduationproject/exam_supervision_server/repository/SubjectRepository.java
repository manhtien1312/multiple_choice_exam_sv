package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Subject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    @Transactional
    @Query(value = "SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Subject s WHERE s.subjectCode=:subjectCode")
    boolean existBySubjectCode(String subjectCode);

    @Query(value = "SELECT s FROM Subject s WHERE s.subjectName=:subjectName")
    Optional<Subject> findBySubjectName(String subjectName);

    @Query(value = "SELECT m.subjects FROM Major m WHERE m.majorName=:majorName")
    List<Subject> findByMajorName(String majorName);
}
