package com.pratham.gearguard.dto.request;

public record UpdateDepartmentRequest(
        String name,
        String description,
        Long managerId
) {}
