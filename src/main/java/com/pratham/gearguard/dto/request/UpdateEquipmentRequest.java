package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.EquipmentStatus;

public record UpdateEquipmentRequest(
        String name,
        String location,
        String specifications,
        EquipmentStatus status,
        Long departmentId,
        Long assignedEmployeeId,
        Long maintenanceTeamId,
        Long defaultTechnicianId
) {}
