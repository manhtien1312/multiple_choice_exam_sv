package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.ClassDto;
import com.graduationproject.exam_supervision_server.dto.request.ClassQueryParam;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Class;
import com.graduationproject.exam_supervision_server.model.Teacher;
import com.graduationproject.exam_supervision_server.repository.ClassRepository;
import com.graduationproject.exam_supervision_server.repository.SubjectRepository;
import com.graduationproject.exam_supervision_server.repository.TeacherRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ClassService;
import com.graduationproject.exam_supervision_server.utils.dtomapper.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ClassMapper classMapper;

    @Override
    public ResponseEntity<List<ClassDto>> getAllClassByTeacher() {
        Teacher teacher = getTeacherFromRequest();
        List<Class> classes = classRepository.findClassByTeacherId(teacher.getId());
        List<ClassDto> res = classes.stream()
                .map(classMapper)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<Class> getClassById(String classId) {
        Class res = classRepository.findById(UUID.fromString(classId)).get();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<List<ClassDto>> searchClass(ClassQueryParam queryParam) {
        List<ClassDto> classes = classRepository.findAll().stream()
                .map(classMapper)
                .toList();
        List<ClassDto> firstFilteredClasses;
        List<ClassDto> res;

        if (queryParam.subjectName() != null) {
            firstFilteredClasses = classes.stream()
                    .filter(classObj -> classObj.subject().equals(queryParam.subjectName()))
                    .toList();
        }
        else {
            firstFilteredClasses = classes;
        }

        if (queryParam.teacherName() != null){
            res = firstFilteredClasses.stream()
                    .filter(classObj -> classObj.teacherName().equals(queryParam.teacherName()))
                    .toList();
        }
        else {
            res = firstFilteredClasses;
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<MessageResponse> createClass(ClassDto classDto) {
        var classObj = Class.builder()
                .className(classDto.className())
                .subject(subjectRepository.findBySubjectName(classDto.subject()).orElseThrow())
                .teacher(getTeacherFromRequest())
                .build();
        classRepository.save(classObj);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm lớp học thành công"));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteClass(String classId) {
        classRepository.deleteById(UUID.fromString(classId));
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Xóa lớp học thành công"));
    }

    private Teacher getTeacherFromRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return teacherRepository.findByAccountUsername(userDetails.getUsername()).orElseThrow();
    }
}
