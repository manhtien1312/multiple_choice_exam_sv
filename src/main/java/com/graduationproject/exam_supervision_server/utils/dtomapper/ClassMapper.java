package com.graduationproject.exam_supervision_server.utils.dtomapper;

import com.graduationproject.exam_supervision_server.dto.ClassDto;
import com.graduationproject.exam_supervision_server.model.Class;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ClassMapper implements Function<Class, ClassDto> {
    @Override
    public ClassDto apply(Class classObj) {
        return new ClassDto(
                classObj.getId().toString(),
                classObj.getClassName(),
                classObj.getSubject().getSubjectName(),
                classObj.getTeacher().getTeacherName()
        );
    }
}
