package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.UserRole;

public record UpdateUserRequest(
        String name,
        String phoneNumber,
        String avatar,
        UserRole role,
        Long departmentId,
        Boolean isActive
) {}
