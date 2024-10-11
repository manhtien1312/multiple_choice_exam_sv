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
@Table(name = "question_bank")
public class QuestionBank {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private Integer type;

    @OneToMany(
            mappedBy = "questionBank",
            cascade = CascadeType.ALL
    )
    private List<Question> questions;

}
