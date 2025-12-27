package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.RequestType;
import com.pratham.gearguard.model.enums.RequestStage;
import com.pratham.gearguard.model.enums.Priority;
import com.pratham.gearguard.model.enums.EquipmentCategory;

import java.time.LocalDateTime;

public record MaintenanceRequestResponse(
        Long id,
        String subject,
        String description,
        RequestType type,
        RequestStage stage,
        Priority priority,
        String equipmentName,
        Long equipmentId,
        EquipmentCategory equipmentCategory, // Auto-filled from equipment
        String maintenanceTeamName,
        Long maintenanceTeamId, // Auto-filled from equipment
        String assignedTechnicianName,
        Long assignedTechnicianId,
        String assignedTechnicianAvatar, // For Kanban board
        String createdByName,
        Long createdById,
        LocalDateTime scheduledDate,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        Integer durationInMinutes,
        String resolutionNotes,
        String scrapReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isOverdue // For visual indicator
) {}
