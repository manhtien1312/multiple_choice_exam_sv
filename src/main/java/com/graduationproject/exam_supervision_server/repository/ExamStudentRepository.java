package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.ExamStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamStudentRepository extends JpaRepository<ExamStudent, UUID> {

    @Query(value = "SELECT es FROM ExamStudent es " +
            "WHERE es.exam.classBelonged.id=:classId AND es.student.id=:studentId " +
            "ORDER BY es.exam.timeStart")
    List<ExamStudent> findAllByClassIdAndStudentId(UUID classId, UUID studentId);

}
