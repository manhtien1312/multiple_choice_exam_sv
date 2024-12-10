package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.model.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Transactional
    @Query(value = "SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.studentCode=:studentCode")
    boolean existByStudentCode(String studentCode);

    @Query(value = "SELECT s FROM Student s WHERE s.studentCode=:studentCode")
    Optional<Student> findByStudentCode(String studentCode);

    @Query(value = "SELECT s FROM Student s WHERE s.account.username=:username")
    Optional<Student> findByAccountUsername(String username);

}
