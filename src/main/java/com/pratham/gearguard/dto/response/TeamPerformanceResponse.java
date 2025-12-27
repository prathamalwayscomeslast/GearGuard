package com.pratham.gearguard.dto.response;

public record TeamPerformanceResponse(
        Long teamId,
        String teamName,
        Long completedRequests,
        Long averageDurationInMinutes,
        Long overdueRequests,
        Double completionRate
) {}
