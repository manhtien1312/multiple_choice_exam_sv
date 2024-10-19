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
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue
    private UUID id;

    private String reportName;

    @ManyToMany
    @JoinTable(
            name = "report_exam-result",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_result_id")
    )
    private List<ExamResult> examResults;

}
