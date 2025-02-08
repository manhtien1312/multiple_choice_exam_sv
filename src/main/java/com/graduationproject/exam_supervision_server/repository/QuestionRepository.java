package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.QuestionType;
import com.graduationproject.exam_supervision_server.model.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    @Query(value = "SELECT q FROM Question q WHERE q.questionCode=:questionCode AND q.questionBank.id=:questionBankId")
    Optional<Question> findByQuestionCode(String questionCode, UUID questionBankId);

    @Query(value = "SELECT q FROM Question q " +
            "WHERE q.questionBank.id=:questionBankId " +
            "AND q.type.typeName=:typeName AND q.level=:level " +
            "ORDER BY FUNCTION('RAND')")
    List<Question> findRandomQuestionsByType(String typeName, int level, UUID questionBankId, Pageable pageable);

    @Query(value = """
        SELECT q.question_code
        FROM question q
        JOIN question_type qt ON q.question_type_id = qt.id
        WHERE qt.type_name = :typeName
          AND q.question_bank_id = :questionBankId
        ORDER BY CAST(SUBSTRING_INDEX(q.question_code, '.', -1) AS UNSIGNED) DESC
        LIMIT 1
        """, nativeQuery = true)
    String findNewestQuestionCode(String typeName, UUID questionBankId);


    /* NV Ngọc sửa
     NM Tiến: Question không có trường subject nên hàm này không chạy được.
     Nếu muốn lấy tất cả câu hỏi ôn tập của 1 subject thì chỉ cần lấy QuestionBank bằng questionBankId
     hoặc lấy QuestionBank bằng subjectId và type
     Vì mỗi Subject chỉ có 2 QuestionBank (không có hơn)
     */
//    List<Question> findBySubjectId(UUID subjectId);
}
