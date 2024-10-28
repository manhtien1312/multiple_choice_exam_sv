package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    @Query(value = "SELECT t FROM Teacher t WHERE t.account.username=:username")
    Optional<Teacher> findByAccountUsername(String username);

}
