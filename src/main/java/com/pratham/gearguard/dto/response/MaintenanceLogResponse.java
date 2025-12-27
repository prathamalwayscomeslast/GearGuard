package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.LogAction;

import java.time.LocalDateTime;

public record MaintenanceLogResponse(
        Long id,
        String userName,
        LogAction action,
        String previousValue,
        String newValue,
        String comment,
        LocalDateTime timestamp
) {}
