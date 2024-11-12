package com.graduationproject.exam_supervision_server.dto;

import java.util.List;

public class ExamResponse {
	
    private String examQuestionCode;
    private int totalQuestions;
    private int theoryQuestions;
    private int practicalQuestions;
    private List<QuestionDto> questions;

}
