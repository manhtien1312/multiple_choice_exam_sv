package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExamResultRepository extends JpaRepository<ExamResult, UUID> {
    List<ExamResult> findByExamId(UUID examId);  // Sử dụng UUID làm kiểu dữ liệu cho examId
    // Tìm tất cả kết quả thi theo ID lớp học
    List<ExamResult> findByExam_ClassBelonged_Id(UUID classId);
}
