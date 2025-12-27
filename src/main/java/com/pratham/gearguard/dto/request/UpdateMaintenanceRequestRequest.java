package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.Priority;

import java.time.LocalDateTime;

public record UpdateMaintenanceRequestRequest(
        String subject,
        String description,
        Priority priority,
        LocalDateTime scheduledDate
) {}
