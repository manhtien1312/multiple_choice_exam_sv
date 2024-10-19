package com.graduationproject.exam_supervision_server.service.serviceinterface;


import com.graduationproject.exam_supervision_server.model.PracticeQuestion;

import java.util.List;

public interface PracticeServiceInterface {
    List<PracticeQuestion> getPracticeQuestionsBySubject(Long subjectId);

    boolean checkAnswer(Long questionId, String selectedAnswer);

    String getExplanation(Long questionId);
}