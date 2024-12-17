package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Major;
import com.graduationproject.exam_supervision_server.repository.MajorRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {

    @Autowired
    private MajorRepository majorRepository;

    @Override
    public ResponseEntity<List<Major>> getAllMajors() {
        List<Major> res = majorRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<MessageResponse> addMajor(Major major) {
        try {
            majorRepository.save(major);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm khoa/ngành mới thành công!"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Vui lòng thử lại sau!"));
        }
    }
}
