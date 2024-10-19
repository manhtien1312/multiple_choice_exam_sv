package com.graduationproject.exam_supervision_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue
    private UUID id;

    private String examName;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classBelonged;

    @OneToOne
    @JoinColumn(name = "exam_question_id")
    private ExamQuestion examQuestion;

    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;

    @ManyToMany
    @JoinTable(
            name = "exam_student",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> allowedStudents;

    @OneToMany(mappedBy = "exam")
    @JsonIgnore
    private List<CheatLog> cheatLogs;

    @OneToMany(mappedBy = "exam")
    @JsonIgnore
    private List<ExamResult> examResults;

}
