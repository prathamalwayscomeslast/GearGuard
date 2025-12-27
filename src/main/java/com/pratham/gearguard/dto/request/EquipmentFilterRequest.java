package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.EquipmentCategory;
import com.pratham.gearguard.model.enums.EquipmentStatus;

public record EquipmentFilterRequest(
        Long departmentId,
        Long assignedEmployeeId,
        EquipmentCategory category,
        EquipmentStatus status,
        Boolean isScrap
) {}
