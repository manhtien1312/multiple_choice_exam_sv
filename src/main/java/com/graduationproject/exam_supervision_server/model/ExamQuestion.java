package com.graduationproject.exam_supervision_server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exam_question")
public class ExamQuestion {

    @Id
    @GeneratedValue
    private UUID id;

    private String examQuestionCode;
    private Integer totalQuestions;
    private Integer theoryQuestions;
    private Integer practicalQuestions;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classBelonged;

    @ManyToMany
    @JoinTable(
            name = "exam-question_question",
            joinColumns = @JoinColumn(name = "exam_question_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions;

    private boolean isUsed;

}
