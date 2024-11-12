package com.graduationproject.exam_supervision_server.service;

import com.graduationproject.exam_supervision_server.dto.ExamRequest;
import com.graduationproject.exam_supervision_server.dto.ExamResponse;
import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import com.graduationproject.exam_supervision_server.model.Question;
import com.graduationproject.exam_supervision_server.repository.ExamQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.graduationproject.exam_supervision_server.utils.DtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExamService {
	
    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    public ExamResponse createExam(ExamRequest request) {
        List<Question> selectedQuestions = new ArrayList<>();
        selectedQuestions.addAll(questionService.getRandomQuestionsByLevel(1, request.getSubject(), request.getLevel1Count()));
        selectedQuestions.addAll(questionService.getRandomQuestionsByLevel(2, request.getSubject(), request.getLevel2Count()));
        selectedQuestions.addAll(questionService.getRandomQuestionsByLevel(3, request.getSubject(), request.getLevel3Count()));

        if (selectedQuestions.size() > request.getTotalQuestions()) {
            selectedQuestions = selectedQuestions.subList(0, request.getTotalQuestions());
        }

        ExamQuestion exam = new ExamQuestion();
        exam.setId(UUID.randomUUID().toString());
        exam.setExamQuestionCode(UUID.randomUUID().toString());
        exam.setTotalQuestions(request.getTotalQuestions());
        exam.setTheoryQuestions(request.getLevel1Count());
        exam.setPracticalQuestions(request.getLevel2Count() + request.getLevel3Count());
        exam.setQuestions(selectedQuestions);
        exam.setIsUsed(false);

        examQuestionRepository.save(exam);
        return DtoMapper.toExamResponse(exam);
    }
}
