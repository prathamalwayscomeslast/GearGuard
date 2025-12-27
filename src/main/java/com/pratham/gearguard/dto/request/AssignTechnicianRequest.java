package com.pratham.gearguard.dto.request;

import jakarta.validation.constraints.NotNull;

public record AssignTechnicianRequest(
        @NotNull(message = "Technician ID is required")
        Long technicianId
) {}
