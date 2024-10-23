package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.request.SignUpRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Account;
import com.graduationproject.exam_supervision_server.model.Teacher;
import com.graduationproject.exam_supervision_server.repository.TeacherRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.AccountService;
import com.graduationproject.exam_supervision_server.service.serviceinterface.TeacherService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<?> getAllTeacher() {
        List<Teacher> teachers = teacherRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(teachers);
    }

    @Override
    public ResponseEntity<Teacher> getTeacherById(String teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(UUID.fromString(teacherId));
        return teacher.map(value -> ResponseEntity.status(HttpStatus.OK).body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @Override
    public ResponseEntity<MessageResponse> addTeacher(Teacher teacher) {
        try {
            Account teacherAccount = accountService.register(new SignUpRequest(teacher.getTeacherCode(), teacher.getTeacherCode(), "teacher"));
            teacher.setAccount(teacherAccount);
            teacherRepository.save(teacher);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm giáo viên thành công!"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error!"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addTeacherByFile(MultipartFile teacherFile) throws IOException {
        List<Teacher> teachers = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(teacherFile.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++){
            XSSFRow row = sheet.getRow(i);

            var teacher = Teacher.builder()
                    .teacherCode(row.getCell(0).getStringCellValue().trim())
                    .teacherName(row.getCell(1).getStringCellValue().trim())
                    .phoneNumber(row.getCell(2).getStringCellValue().trim())
                    .email(row.getCell(3).getStringCellValue().trim())
                    .build();

            Account account = accountService.register(new SignUpRequest(teacher.getTeacherCode(), teacher.getTeacherCode(), "teacher"));
            teacher.setAccount(account);
            teachers.add(teacher);
        }
        teacherRepository.saveAll(teachers);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm danh sách giáo viên thành công!"));
    }

    @Override
    public ResponseEntity<MessageResponse> modifyTeacher(String teacherId, Teacher teacher) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(UUID.fromString(teacherId));
        if(optionalTeacher.isPresent()){
            Teacher savedTeacher = optionalTeacher.get();
            savedTeacher.setTeacherCode(teacher.getTeacherCode());
            savedTeacher.setTeacherName(teacher.getTeacherName());
            savedTeacher.setPhoneNumber(teacher.getPhoneNumber());
            savedTeacher.setEmail(teacher.getEmail());
            teacherRepository.save(savedTeacher);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Thông tin giáo viên đã được cập nhật!"));
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("Giáo viên không tồn tại"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteTeacherById(String teacherId) {
        teacherRepository.deleteById(UUID.fromString(teacherId));
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Xóa giáo viên thành công!"));
    }
}
