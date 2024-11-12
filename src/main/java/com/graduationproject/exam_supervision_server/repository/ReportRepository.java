package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    // Tìm kiếm báo cáo theo ID kỳ thi
    List<Report> findByExamId(UUID examId);
}
