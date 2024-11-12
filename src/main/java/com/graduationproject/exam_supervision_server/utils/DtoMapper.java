package com.graduationproject.exam_supervision_server.utils;

import com.graduationproject.exam_supervision_server.dto.ExamResponse;
import com.graduationproject.exam_supervision_server.dto.QuestionDto;
import com.graduationproject.exam_supervision_server.model.ExamQuestion;
import com.graduationproject.exam_supervision_server.model.Question;

import java.util.stream.Collectors;

public class DtoMapper {
	
	public static ExamResponse toExamResponse(ExamQuestion exam) {
        ExamResponse response = new ExamResponse();
        response.setExamQuestionCode(exam.getExamQuestionCode());
        response.setTotalQuestions(exam.getTotalQuestions());
        response.setTheoryQuestions(exam.getTheoryQuestions());
        response.setPracticalQuestions(exam.getPracticalQuestions());
        response.setQuestions(
            exam.getQuestions().stream()
                .map(DtoMapper::toQuestionDto)
                .collect(Collectors.toList())
        );
        return response;
    }

    private static QuestionDto toQuestionDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setQuestionContent(question.getQuestionContent());
        dto.setLevel(question.getLevel());
        return dto;
    }

}
