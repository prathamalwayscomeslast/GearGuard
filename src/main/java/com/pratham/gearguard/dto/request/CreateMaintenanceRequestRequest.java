package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.RequestType;
import com.pratham.gearguard.model.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateMaintenanceRequestRequest(
        @NotBlank(message = "Subject is required")
        String subject,

        String description,

        @NotNull(message = "Request type is required")
        RequestType type,

        Priority priority,

        @NotNull(message = "Equipment ID is required")
        Long equipmentId,

        LocalDateTime scheduledDate // For preventive maintenance
) {}
