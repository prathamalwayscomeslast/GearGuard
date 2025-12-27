package com.pratham.gearguard.dto.response;

public record DepartmentResponse(
        Long id,
        String name,
        String description,
        String managerName,
        Long managerId
) {}
