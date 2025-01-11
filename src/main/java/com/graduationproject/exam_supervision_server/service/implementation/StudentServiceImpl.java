package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.request.SignUpRequest;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Account;
import com.graduationproject.exam_supervision_server.model.Class;
import com.graduationproject.exam_supervision_server.model.Major;
import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.repository.ClassRepository;
import com.graduationproject.exam_supervision_server.repository.MajorRepository;
import com.graduationproject.exam_supervision_server.repository.StudentRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.AccountService;
import com.graduationproject.exam_supervision_server.service.serviceinterface.StudentService;
import jakarta.transaction.Transactional;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<Page<Student>> getAllStudents(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("studentCode"));
        Page<Student> res = studentRepository.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<List<String>> getAllCohort() {
        List<String> res = studentRepository.getAllCohort();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<Page<Student>> filterStudent(String searchText, String majorName, String cohort, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("studentCode"));
        Page<Student> res = studentRepository.findByMajorAndCohortAndSearchText(searchText, majorName, cohort, pageable);
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

            String [] studentNameWords = student.getStudentFullName().split(" ");
            student.setStudentFirstName(studentNameWords[studentNameWords.length-1]);

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
        if(studentRepository.existByStudentCode(student.getStudentCode())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Tồn tại mã sinh viên trong hệ thống. Vui lòng kiểm tra lại!"));
        }
        Account studentAccount = accountService.register(new SignUpRequest(student.getStudentCode(), student.getStudentCode(), "student"));
        student.setAccount(studentAccount);
        String [] studentNameWords = student.getStudentFullName().split(" ");
        student.setStudentFirstName(studentNameWords[studentNameWords.length-1]);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm sinh viên thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> addStudentByFile(MultipartFile studentFile) throws IOException {
        try {
            List<Student> students = new ArrayList<>();
            List<Major> majors = majorRepository.findAll();
            XSSFWorkbook workbook = new XSSFWorkbook(studentFile.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++){
                XSSFRow row = sheet.getRow(i);

                String [] studentNameWords = row.getCell(2).getStringCellValue().trim().split(" ");
                String majorName = row.getCell(4).getStringCellValue().trim();

                Optional<Major> major = majors.stream()
                        .filter(major1 -> major1.getMajorName().equals(majorName))
                        .findFirst();
                if(major.isEmpty()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Tồn tại dữ liệu khoa/ngành không chính xác. Vui lòng kiểm tra file dữ liệu!"));
                }

                String studentCode = row.getCell(1).getStringCellValue().trim();
                if(studentRepository.existByStudentCode(studentCode)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Tồn tại mã sinh viên trong hệ thống. Vui lòng kiểm tra lại!"));
                }

                var student = Student.builder()
                        .studentCode(studentCode)
                        .studentFullName(row.getCell(2).getStringCellValue().trim())
                        .studentFirstName(studentNameWords[studentNameWords.length-1])
                        .cohort(row.getCell(3).getStringCellValue())
                        .major(major.get())
                        .email(row.getCell(5).getStringCellValue())
                        .build();

                Account studentAccount = accountService.register(new SignUpRequest(student.getStudentCode(), student.getStudentCode(), "student"));
                student.setAccount(studentAccount);
                students.add(student);
            }
            studentRepository.saveAll(students);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm sinh viên thành công"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi server. Vui lòng thử lại sau!"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> addStudentToClassByFile(String classId, MultipartFile studentFile) throws IOException {
        Class classObj = classRepository.findById(UUID.fromString(classId)).get();

        XSSFWorkbook workbook = new XSSFWorkbook(studentFile.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++){
            XSSFRow row = sheet.getRow(i);

            String [] studentNameWords = row.getCell(2).getStringCellValue().trim().split(" ");

            var student = Student.builder()
                    .studentCode(row.getCell(1).getStringCellValue().trim())
                    .studentFullName(row.getCell(2).getStringCellValue().trim())
                    .studentFirstName(studentNameWords[studentNameWords.length-1])
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
    public ResponseEntity<MessageResponse> updateStudent(Student student) {
        Student savedStudent = studentRepository.findByStudentCode(student.getStudentCode()).get();
        String [] studentNameWords = student.getStudentFullName().split(" ");
        savedStudent.setStudentFirstName(studentNameWords[studentNameWords.length-1]);
        savedStudent.setStudentFullName(student.getStudentFullName());
        savedStudent.setEmail(student.getEmail());
        savedStudent.setCohort(student.getCohort());
        savedStudent.setMajor(student.getMajor());
        studentRepository.save(savedStudent);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Cập nhật thông tin sinh viên thành công"));
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
