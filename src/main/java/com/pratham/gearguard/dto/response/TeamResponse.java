package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.TeamSpecialization;

import java.util.List;

public record TeamResponse(
        Long id,
        String name,
        String description,
        TeamSpecialization specialization,
        String teamLeadName,
        Long teamLeadId,
        List<UserResponse> members,
        Boolean isActive
) {}
