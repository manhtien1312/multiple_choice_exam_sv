package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Major;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {

    @Transactional
    @Query(value = "SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Major m WHERE m.majorName=:majorName")
    boolean existByMajorName(String majorName);

    @Query(value = "SELECT m FROM Major m WHERE m.majorName=:majorName")
    Optional<Major> findByMajorName(String majorName);

}
