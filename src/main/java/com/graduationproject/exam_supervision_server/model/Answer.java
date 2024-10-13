package com.graduationproject.exam_supervision_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String answerContent;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isCorrect;

}
