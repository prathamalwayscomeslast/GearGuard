package com.pratham.gearguard.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddTeamMemberRequest(
        @NotNull(message = "User ID is required")
        Long userId
) {}
