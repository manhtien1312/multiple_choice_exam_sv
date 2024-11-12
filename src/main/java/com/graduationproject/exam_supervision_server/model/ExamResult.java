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
    @Table(name = "exam_result")
    public class ExamResult {

        @Id
        @GeneratedValue
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "exam_id")
        private Exam exam;

        @ManyToOne
        @JoinColumn(name = "student_id")
        private Student student;

        private Integer correctQuestions;
        private Integer wrongQuestions;
        private Integer blankQuestions;
        private Double score;

        @ManyToMany(mappedBy = "examResults")
        private List<Report> reports;
    }
