package com.pratham.gearguard.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateDepartmentRequest(
        @NotBlank(message = "Department name is required")
        String name,

        String description,

        Long managerId
) {}
