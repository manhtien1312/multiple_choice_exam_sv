package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.SubjectDto;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.QuestionBank;
import com.graduationproject.exam_supervision_server.model.Subject;
import com.graduationproject.exam_supervision_server.repository.SubjectRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.SubjectService;
import com.graduationproject.exam_supervision_server.utils.dtomapper.SubjectMapper;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public ResponseEntity<?> getAllSubjects() {
        try {
            List<Subject> subjects = subjectRepository.findAll();
            if(subjects.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            List<SubjectDto> res = subjects.stream()
                    .map(subjectMapper)
                    .toList();
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<?> getSubjectById(String id) {
        Optional<Subject> subject = subjectRepository.findById(UUID.fromString(id));
        if (subject.isEmpty()) {
            throw new RuntimeException("Môn học không tồn tại, vui lòng kiểm tra lại ID");
        }
        return ResponseEntity.status(HttpStatus.OK).body(subject.map(subjectMapper));
    }

    @Override
    public ResponseEntity<MessageResponse> addSubject(SubjectDto subjectDto) {
        try {
            if(subjectRepository.existBySubjectCode(subjectDto.subjectCode())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Mã môn học đã tồn tại"));
            }
            var subject = Subject.builder()
                            .subjectCode(subjectDto.subjectCode())
                            .subjectName(subjectDto.subjectName())
                            .questionBanks(new ArrayList<>())
                            .build();
            subject.getQuestionBanks().add(QuestionBank.builder()
                            .subject(subject)
                            .type(0)
                            .build());
            subject.getQuestionBanks().add(QuestionBank.builder()
                            .subject(subject)
                            .type(1)
                            .build());
            subjectRepository.save(subject);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm môn học thành công"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử Lại Sau!"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> modifySubject(String id, SubjectDto subjectDto) {
        try {
            Subject subject = subjectRepository.findById(UUID.fromString(id))
                    .orElseThrow();
            subject.setSubjectCode(subjectDto.subjectCode());
            subject.setSubjectName(subjectDto.subjectName());
            subjectRepository.save(subject);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Sửa môn học thành công"));
        } catch (Exception e){
            throw new RuntimeException("Môn học không tồn tại, vui lòng kiểm tra lại ID");
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteSubject(String id) {
        Optional<Subject> subject = subjectRepository.findById(UUID.fromString(id));
        if(subject.isEmpty()){
            throw new RuntimeException("Môn học không tồn tại, vui lòng kiểm tra lại ID");
        } else {
            subjectRepository.deleteById(UUID.fromString(id));
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Xóa môn học thành công"));
        }
    }
}
