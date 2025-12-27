package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.RequestStage;
import com.pratham.gearguard.model.enums.Priority;

import java.time.LocalDateTime;

public record MaintenanceRequestSummaryResponse(
        Long id,
        String subject,
        RequestStage stage,
        Priority priority,
        String equipmentName,
        String assignedTechnicianName,
        LocalDateTime scheduledDate,
        Boolean isOverdue
) {}
