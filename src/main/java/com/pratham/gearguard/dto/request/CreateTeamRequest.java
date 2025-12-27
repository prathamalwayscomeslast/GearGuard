package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.TeamSpecialization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateTeamRequest(
        @NotBlank(message = "Team name is required")
        String name,

        String description,

        @NotNull(message = "Specialization is required")
        TeamSpecialization specialization,

        Set<Long> memberIds,

        Long teamLeadId
) {}
