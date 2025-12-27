package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.UserRole;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        String phoneNumber,
        String avatar,
        String departmentName,
        Long departmentId,
        Boolean isActive
) {}
