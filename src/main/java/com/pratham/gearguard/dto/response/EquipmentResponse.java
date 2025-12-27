package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.EquipmentCategory;
import com.pratham.gearguard.model.enums.EquipmentStatus;

import java.time.LocalDate;

public record EquipmentResponse(
        Long id,
        String name,
        String serialNumber,
        EquipmentCategory category,
        String location,
        LocalDate purchaseDate,
        LocalDate warrantyExpiryDate,
        String specifications,
        String departmentName,
        Long departmentId,
        String assignedEmployeeName,
        Long assignedEmployeeId,
        String maintenanceTeamName,
        Long maintenanceTeamId,
        String defaultTechnicianName,
        Long defaultTechnicianId,
        EquipmentStatus status,
        Boolean isScrap,
        Long openRequestsCount // For smart button
) {}
