package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.request.SignUpRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Account;
import com.graduationproject.exam_supervision_server.model.Class;
import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.repository.ClassRepository;
import com.graduationproject.exam_supervision_server.repository.StudentRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.AccountService;
import com.graduationproject.exam_supervision_server.service.serviceinterface.StudentService;
import jakarta.transaction.Transactional;
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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> res = studentRepository.findAll();
        res.sort(Comparator.comparing(Student::getStudentCode));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<MessageResponse> addStudentToClass(String classId, Student student) {
        Class classObj = classRepository.findById(UUID.fromString(classId)).get();
        if(studentRepository.existByStudentCode(student.getStudentCode())){
            Student savedStudent = studentRepository.findByStudentCode(student.getStudentCode()).get();
            classObj.getStudents().add(savedStudent);
            classRepository.save(classObj);
        }
        else {
            Account studentAccount = accountService.register(new SignUpRequest(student.getStudentCode(), student.getStudentCode(), "student"));
            student.setAccount(studentAccount);

            List<Class> studentClasses = new ArrayList<>();
            studentClasses.add(classObj);

            Student savedStudent = studentRepository.save(student);
            classObj.getStudents().add(savedStudent);
            classRepository.save(classObj);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm sinh viên thành công"));
    }

    @Override
    public ResponseEntity<MessageResponse> addNewStudent(Student student) {
        Account studentAccount = accountService.register(new SignUpRequest(student.getStudentCode(), student.getStudentCode(), "student"));
        student.setAccount(studentAccount);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm sinh viên thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> addStudentByFile(MultipartFile studentFile) throws IOException {
        List<Student> students = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(studentFile.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++){
            XSSFRow row = sheet.getRow(i);

            var student = Student.builder()
                    .studentCode(row.getCell(1).getStringCellValue().trim())
                    .studentName(row.getCell(2).getStringCellValue().trim())
                    .build();

            Account studentAccount = accountService.register(new SignUpRequest(student.getStudentCode(), student.getStudentCode(), "student"));
            student.setAccount(studentAccount);
            students.add(student);
        }
        studentRepository.saveAll(students);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm sinh viên thành công"));
    }

    @Override
    public ResponseEntity<MessageResponse> addStudentToClassByFile(String classId, MultipartFile studentFile) throws IOException {
        Class classObj = classRepository.findById(UUID.fromString(classId)).get();

        XSSFWorkbook workbook = new XSSFWorkbook(studentFile.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++){
            XSSFRow row = sheet.getRow(i);

            var student = Student.builder()
                    .studentCode(row.getCell(1).getStringCellValue().trim())
                    .studentName(row.getCell(2).getStringCellValue().trim())
                    .build();

            if(studentRepository.existByStudentCode(student.getStudentCode())){
                Student savedStudent = studentRepository.findByStudentCode(student.getStudentCode()).get();
                classObj.getStudents().add(savedStudent);
            }
            else {
                Account studentAccount = accountService.register(new SignUpRequest(student.getStudentCode(), student.getStudentCode(), "student"));
                student.setAccount(studentAccount);

                List<Class> studentClasses = new ArrayList<>();
                studentClasses.add(classObj);

                Student savedStudent = studentRepository.save(student);
                classObj.getStudents().add(savedStudent);
            }
        }

        classRepository.save(classObj);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm sinh viên thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> removeStudentFromClass(String classId, List<String> selectedStudents) {
        try{
            Class classObj = classRepository.findById(UUID.fromString(classId)).get();
            List<Student> classStudents = classObj.getStudents();
            List<Student> updatedStudents = classStudents.stream()
                    .filter(student -> !selectedStudents.contains(student.getId().toString()))
                    .collect(Collectors.toList());
            classObj.setStudents(updatedStudents);
            classRepository.save(classObj);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Đã xóa sinh viên khỏi lớp"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi"));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> deleteStudent(List<String> selectedStudents) {
        for(String studentId : selectedStudents){
            Student student = studentRepository.findById(UUID.fromString(studentId)).get();
            for(Class cls : student.getClasses()){
                cls.getStudents().remove(student);
                classRepository.save(cls);
            }
            studentRepository.deleteById(UUID.fromString(studentId));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Xóa sinh viên thành công"));
    }
}
