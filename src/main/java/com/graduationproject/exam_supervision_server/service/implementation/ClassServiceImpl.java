package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.ClassDto;
import com.graduationproject.exam_supervision_server.dto.response.MessageResponse;
import com.graduationproject.exam_supervision_server.model.Class;
import com.graduationproject.exam_supervision_server.model.Student;
import com.graduationproject.exam_supervision_server.model.Subject;
import com.graduationproject.exam_supervision_server.model.Teacher;
import com.graduationproject.exam_supervision_server.repository.ClassRepository;
import com.graduationproject.exam_supervision_server.repository.StudentRepository;
import com.graduationproject.exam_supervision_server.repository.SubjectRepository;
import com.graduationproject.exam_supervision_server.repository.TeacherRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ClassService;
import com.graduationproject.exam_supervision_server.utils.dtomapper.ClassMapper;
import jakarta.transaction.Transactional;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassMapper classMapper;

    @Override
    public ResponseEntity<List<ClassDto>> getAllClasses() {
        List<Class> classes = classRepository.findAll();
        classes.sort(
                Comparator.comparing((Class c) -> c.getSubject().getSubjectName(), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Class::getClassName, String.CASE_INSENSITIVE_ORDER)
        );
        List<ClassDto> res = classes.stream()
                .map(classMapper)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

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
    public ResponseEntity<List<ClassDto>> getAllClassByStudent() {
        List<Class> classes = getStudentFromRequest().getClasses();
        List<ClassDto> res = classes.stream()
                .map(classMapper)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<?> getAllClassBySubject(String subjectId) {
        try {
            List<Class> classes = classRepository.findClassBySubject(UUID.fromString(subjectId));
            List<ClassDto> res = classes.stream()
                    .map(classMapper)
                    .toList();
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Lỗi Server. Thử lại sau!"));
        }
    }

    @Override
    public ResponseEntity<Class> getClassById(String classId) {
        Class res = classRepository.findById(UUID.fromString(classId)).get();
        res.getStudents().sort(Comparator.comparing(Student::getStudentFirstName));
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    public ResponseEntity<List<ClassDto>> searchClass(String searchText) {
        List<ClassDto> classes = classRepository.findAll().stream()
                .map(classMapper)
                .toList();

        String lowerSearchText = searchText.toLowerCase();
        List<ClassDto> res = classes.stream()
                .filter(classDto -> classDto.subject().toLowerCase().contains(lowerSearchText) ||
                        classDto.teacherName().toLowerCase().contains(lowerSearchText) ||
                        classDto.className().toLowerCase().contains(lowerSearchText))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @Override
    @Transactional
    public ResponseEntity<MessageResponse> createClass(String subjectName, MultipartFile classFile) throws IOException {
//        var classObj = Class.builder()
//                .className(classDto.className())
//                .subject(subjectRepository.findBySubjectName(classDto.subject()).orElseThrow())
//                .teacher(teacherRepository.findByTeacherName(classDto.teacherName()).get())
//                .build();
//        classRepository.save(classObj);
//        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thêm lớp học thành công"));
        Subject subject = subjectRepository.findBySubjectName(subjectName).orElseThrow();

        XSSFWorkbook workbook = new XSSFWorkbook(classFile.getInputStream());
        for(int i=0; i<workbook.getNumberOfSheets(); i++){
            XSSFSheet sheet = workbook.getSheetAt(i);

            String className = sheet.getSheetName();

            String teacherCode = sheet.getRow(1).getCell(1).getStringCellValue().trim();
            Teacher teacher = teacherRepository.findByTeacherCode(teacherCode).orElseThrow();

            Class newClass = Class.builder()
                            .className(className)
                            .subject(subject)
                            .teacher(teacher)
                            .build();

            Class savedClass = classRepository.save(newClass);

            List<Student> students = new ArrayList<>();
            for(int j=6; j<sheet.getPhysicalNumberOfRows(); j++){
                XSSFRow row = sheet.getRow(j);

                String studentCode = row.getCell(0).getStringCellValue().trim();
                String studentName = row.getCell(1).getStringCellValue().trim();

                if(!studentRepository.existByStudentCode(studentCode)){
                    String message = "Sinh viên chưa có trong hệ thống: " + studentCode + " - " + studentName;
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
                } else {
                    Student savedStudent = studentRepository.findByStudentCode(studentCode).get();
                    students.add(savedStudent);
                }
            }
            savedClass.setStudents(students);
            classRepository.save(savedClass);
        }
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

    private Student getStudentFromRequest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return studentRepository.findByAccountUsername(userDetails.getUsername()).orElseThrow();
    }
}
