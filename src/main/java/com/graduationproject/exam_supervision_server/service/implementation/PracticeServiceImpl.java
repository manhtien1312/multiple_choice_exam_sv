package com.graduationproject.exam_supervision_server.service.implementation;



import com.graduationproject.exam_supervision_server.model.PracticeQuestion;
import com.graduationproject.exam_supervision_server.repository.PracticeQuestionRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.PracticeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PracticeServiceImpl implements PracticeServiceInterface {

    @Autowired
    private PracticeQuestionRepository practiceQuestionRepository;

    @Override
    public List<PracticeQuestion> getPracticeQuestionsBySubject(Long subjectId) {
        return practiceQuestionRepository.findBySubjectId(subjectId);
    }

    @Override
    public boolean checkAnswer(Long questionId, String selectedAnswer) {
        PracticeQuestion question = practiceQuestionRepository.findById(questionId).orElseThrow();
        return question.getCorrectAnswer().equals(selectedAnswer);
    }

    @Override
    public String getExplanation(Long questionId) {
        PracticeQuestion question = practiceQuestionRepository.findById(questionId).orElseThrow();
        return question.getExplanation();
    }
}
