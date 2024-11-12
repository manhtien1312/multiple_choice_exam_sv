package com.graduationproject.exam_supervision_server.service;

import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.model.Subject;
import com.graduationproject.exam_supervision_server.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
	
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getRandomQuestionsByLevel(int level, Subject subject, int count) {
        return questionRepository.findByLevelAndQuestionBank_Subject(level, subject, PageRequest.of(0, count));
    }

}
