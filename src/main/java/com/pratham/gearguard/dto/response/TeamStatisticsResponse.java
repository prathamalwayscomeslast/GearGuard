package com.pratham.gearguard.dto.response;

public record TeamStatisticsResponse(
        Long teamId,
        String teamName,
        Long totalRequests,
        Long newRequests,
        Long inProgressRequests,
        Long repairedRequests,
        Long scrapRequests
) {}
