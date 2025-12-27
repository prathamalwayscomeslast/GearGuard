package com.pratham.gearguard.dto.response;

public record DashboardStatsResponse(
        Long totalEquipment,
        Long totalRequests,
        Long openRequests,
        Long overdueRequests,
        Long equipmentUnderMaintenance,
        Long scrappedEquipment
) {}
