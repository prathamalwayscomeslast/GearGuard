package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.EquipmentCategory;

public record CategoryStatisticsResponse(
        EquipmentCategory category,
        Long requestCount
) {}
