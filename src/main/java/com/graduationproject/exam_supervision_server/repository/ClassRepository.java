package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassRepository extends JpaRepository<Class, UUID> {

    @Query(value = "SELECT c FROM Class c WHERE c.teacher.id=:teacherId ORDER BY c.className")
    List<Class> findClassByTeacherId(UUID teacherId);

    @Query(value = "SELECT c FROM Class c WHERE c.subject.id=:subjectId ORDER BY c.className")
    List<Class> findClassBySubject(UUID subjectId);

}
