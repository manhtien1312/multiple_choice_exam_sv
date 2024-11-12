package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.dto.ClassStatisticsDTO;
import com.graduationproject.exam_supervision_server.model.ExamResult;
import com.graduationproject.exam_supervision_server.repository.ExamResultRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.ClassStatisticsService;
import com.graduationproject.exam_supervision_server.utils.dtomapper.ClassStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClassStatisticsServiceImpl implements ClassStatisticsService {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private ClassStatisticsMapper classStatisticsMapper;

    @Override
    public ClassStatisticsDTO getClassStatistics(UUID classId) {
        List<ExamResult> results = examResultRepository.findByExam_ClassBelonged_Id(classId);

        if (results.isEmpty()) {
            throw new RuntimeException("Không có kết quả thi cho lớp này.");
        }

        String className = results.get(0).getExam().getClassBelonged().getClassName();
        return classStatisticsMapper.toDTO(classId, className, results);
    }
}
