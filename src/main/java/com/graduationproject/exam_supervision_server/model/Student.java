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
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue
    private UUID id;

    private String studentCode;
    private String studentFullName;
    private String studentFirstName;
    private String email;
    private String cohort;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private List<Class> classes;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<ExamStudent> examStudents;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<CheatLog> cheatLogs;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<ExamResult> examResults;

}
