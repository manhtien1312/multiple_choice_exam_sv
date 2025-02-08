package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    @Query(value = "SELECT e FROM Exam e WHERE e.classBelonged.id=:classId ORDER BY e.timeStart")
    List<Exam> findAllByClassId(UUID classId);

}
