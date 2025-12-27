package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.RequestStage;
import com.pratham.gearguard.model.enums.Priority;

import java.time.LocalDateTime;

public record KanbanCardResponse(
        Long id,
        String subject,
        String description,
        RequestStage stage,
        Priority priority,
        String equipmentName,
        Long equipmentId,
        UserSummaryResponse assignedTechnician, // With avatar
        LocalDateTime scheduledDate,
        LocalDateTime createdAt,
        Boolean isOverdue
) {}
