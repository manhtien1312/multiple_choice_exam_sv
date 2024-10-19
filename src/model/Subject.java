package com.graduationproject.exam_supervision_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue
    private UUID id;

    private String subjectCode;
    private String subjectName;

    @OneToMany(
            mappedBy = "subject",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<QuestionBank> questionBanks;

    @OneToMany(
            mappedBy = "subject",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Class> classes;

    @OneToMany(
            mappedBy = "subject",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Exam> exams;

}
