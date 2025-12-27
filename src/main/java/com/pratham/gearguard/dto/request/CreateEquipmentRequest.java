package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.EquipmentCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateEquipmentRequest(
        @NotBlank(message = "Equipment name is required")
        String name,

        @NotBlank(message = "Serial number is required")
        String serialNumber,

        @NotNull(message = "Category is required")
        EquipmentCategory category,

        @NotBlank(message = "Location is required")
        String location,

        @NotNull(message = "Purchase date is required")
        LocalDate purchaseDate,

        LocalDate warrantyExpiryDate,

        String specifications,

        Long departmentId,

        Long assignedEmployeeId,

        @NotNull(message = "Maintenance team is required")
        Long maintenanceTeamId,

        @NotNull(message = "Default technician is required")
        Long defaultTechnicianId
) {}
