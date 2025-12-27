package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.TeamSpecialization;

public record UpdateTeamRequest(
        String name,
        String description,
        TeamSpecialization specialization,
        Long teamLeadId,
        Boolean isActive
) {}
