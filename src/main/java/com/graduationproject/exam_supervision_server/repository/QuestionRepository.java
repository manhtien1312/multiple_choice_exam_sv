package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.Subject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

    List<Question> findByLevelAndQuestionBank_Subject(int level, Subject subject, Pageable pageable);

}
