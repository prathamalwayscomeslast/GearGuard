package com.pratham.gearguard.dto.response;

public record TeamSummaryResponse(
        Long id,
        String name,
        Integer memberCount
) {}
