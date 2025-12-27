package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.EquipmentCategory;

public record EquipmentSummaryResponse(
        Long id,
        String name,
        String serialNumber,
        EquipmentCategory category,
        String location
) {}
