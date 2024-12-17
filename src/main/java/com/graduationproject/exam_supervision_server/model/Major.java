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
@Table(name = "major")
public class Major {

    @Id
    @GeneratedValue
    private UUID id;

    private String majorName;

    @OneToMany(mappedBy = "major")
    @JsonIgnore
    private List<Subject> subjects;

    @OneToMany(mappedBy = "major")
    @JsonIgnore
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "major")
    @JsonIgnore
    private List<Student> students;

}
