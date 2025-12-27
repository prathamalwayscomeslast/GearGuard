package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.RequestType;
import com.pratham.gearguard.model.enums.RequestStage;

import java.time.LocalDateTime;

public record CalendarEventResponse(
        Long id,
        String subject,
        RequestType type,
        RequestStage stage,
        String equipmentName,
        String assignedTechnicianName,
        LocalDateTime scheduledDate,
        Integer durationInMinutes
) {}
