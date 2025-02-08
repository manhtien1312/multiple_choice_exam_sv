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
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
    public void generateListStudentExcel(String classId, HttpServletResponse response) throws IOException {
        Class classDto = classRepository.findById(UUID.fromString(classId)).get();
        List<Student> students = classDto.getStudents();

        XSSFWorkbook workbook = new XSSFWorkbook();

        // Font chữ in đậm và căn giữa ô
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Wrap text
        CellStyle wrapTextStyle = workbook.createCellStyle();
        wrapTextStyle.setWrapText(true);
        wrapTextStyle.setAlignment(HorizontalAlignment.CENTER);
        wrapTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFSheet sheet = workbook.createSheet("Danh sách sinh viên");
        XSSFRow titleRow = sheet.createRow(0);

        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);

        XSSFCell studentCodeCell = titleRow.createCell(0);
        studentCodeCell.setCellValue("Mã sinh viên");
        studentCodeCell.setCellStyle(headerStyle);

        XSSFCell studentNameCell = titleRow.createCell(1);
        studentNameCell.setCellValue("Họ và tên");
        studentNameCell.setCellStyle(headerStyle);

        students.sort(Comparator.comparing(Student::getStudentFirstName));
        int rowIndex = 1;
        for(Student student : students){
            XSSFRow row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(student.getStudentCode());
            row.createCell(1).setCellValue(student.getStudentFullName());
            rowIndex++;
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
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
        Subject subject = subjectRepository.findBySubjectName(subjectName).orElseThrow();

        XSSFWorkbook workbook = new XSSFWorkbook(classFile.getInputStream());
        for(int i=0; i<workbook.getNumberOfSheets(); i++){
            XSSFSheet sheet = workbook.getSheetAt(i);

            String className = sheet.getSheetName();

            if(classRepository.existBySubjectAndClassName(subject.getSubjectName(), className)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("File chứa lớp học đã tồn tại. Vui lòng kiểm tra lại!"));
            }

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
