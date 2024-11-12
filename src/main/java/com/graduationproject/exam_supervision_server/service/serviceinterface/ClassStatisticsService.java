package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.dto.ClassStatisticsDTO;
import java.util.UUID;

public interface ClassStatisticsService {
    ClassStatisticsDTO getClassStatistics(UUID classId);
}
