package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    @Query(value = "SELECT q FROM Question q WHERE q.questionCode=:questionCode")
    Optional<Question> findByQuestionCode(String questionCode);

    /* NV Ngọc sửa
     NM Tiến: Question không có trường subject nên hàm này không chạy được.
     Nếu muốn lấy tất cả câu hỏi ôn tập của 1 subject thì chỉ cần lấy QuestionBank bằng questionBankId
     hoặc lấy QuestionBank bằng subjectId và type
     Vì mỗi Subject chỉ có 2 QuestionBank (không có hơn)
     */
//    List<Question> findBySubjectId(UUID subjectId);
}
