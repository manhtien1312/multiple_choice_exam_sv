package com.graduationproject.exam_supervision_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue
    private UUID id;

    private String teacherCode;
    private String teacherName;
    private String phoneNumber;
    private String email;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @OneToMany(
            mappedBy = "teacher",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Class> classes;

    @OneToMany(
            mappedBy = "createdBy",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<ExamQuestion> examQuestions;

}
