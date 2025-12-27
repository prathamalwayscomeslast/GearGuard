package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.Priority;
import com.pratham.gearguard.model.enums.RequestStage;

import java.time.LocalDateTime;

public record OverdueRequestResponse(
        Long id,
        String subject,
        String equipmentName,
        String maintenanceTeamName,
        String assignedTechnicianName,
        Priority priority,
        RequestStage stage,
        LocalDateTime scheduledDate,
        Long daysOverdue
) {}
