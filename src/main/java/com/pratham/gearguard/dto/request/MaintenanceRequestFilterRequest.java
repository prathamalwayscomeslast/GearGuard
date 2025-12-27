package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.RequestStage;
import com.pratham.gearguard.model.enums.RequestType;
import com.pratham.gearguard.model.enums.Priority;

import java.time.LocalDateTime;

public record MaintenanceRequestFilterRequest(
        Long equipmentId,
        Long maintenanceTeamId,
        Long assignedTechnicianId,
        RequestStage stage,
        RequestType type,
        Priority priority,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean onlyOverdue
) {}
