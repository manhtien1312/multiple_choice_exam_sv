package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.model.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query(value = "SELECT s FROM Student s " +
            "WHERE (:majorName IS NULL OR :majorName='' OR s.major.majorName=:majorName) " +
            "AND (:cohort IS NULL OR :cohort='' OR s.cohort=:cohort) " +
            "AND (:searchText IS NULL OR :searchText='' OR LOWER(s.studentFullName) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    Page<Student> findByMajorAndCohortAndSearchText(
            @Param("searchText") String searchText,
            @Param("majorName") String majorName,
            @Param("cohort") String cohort,
            Pageable pageable);

    @Query(value = "SELECT DISTINCT(s.cohort) FROM Student s ORDER BY s.cohort")
    List<String> getAllCohort();

}
