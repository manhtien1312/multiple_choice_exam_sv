package com.graduationproject.exam_supervision_server.utils.dtomapper;

import com.graduationproject.exam_supervision_server.dto.SubjectDto;
import com.graduationproject.exam_supervision_server.model.Subject;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SubjectMapper implements Function<Subject, SubjectDto> {
    @Override
    public SubjectDto apply(Subject subject) {
        return new SubjectDto(
                subject.getId(),
                subject.getSubjectCode(),
                subject.getSubjectName(),
                null
        );
    }
}
