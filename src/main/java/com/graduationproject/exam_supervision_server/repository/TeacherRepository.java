package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    @Transactional
    @Query(value = "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Teacher t WHERE t.teacherCode=:teacherCode")
    boolean existByTeacherCode(String teacherCode);

    @Query(value = "SELECT t FROM Teacher t WHERE t.account.username=:username")
    Optional<Teacher> findByAccountUsername(String username);

    @Query(value = "SELECT t FROM Teacher t WHERE t.teacherCode=:teacherCode")
    Optional<Teacher> findByTeacherCode(String teacherCode);

    @Query(value = "SELECT t FROM Teacher t WHERE t.major.majorName=:majorName")
    List<Teacher> findByMajorName(String majorName);

}
