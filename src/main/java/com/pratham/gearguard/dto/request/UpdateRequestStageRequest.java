package com.pratham.gearguard.dto.request;

import com.pratham.gearguard.model.enums.RequestStage;
import jakarta.validation.constraints.NotNull;

public record UpdateRequestStageRequest(
        @NotNull(message = "Stage is required")
        RequestStage stage,

        Integer durationInMinutes, // For REPAIRED stage

        String resolutionNotes, // For REPAIRED stage

        String scrapReason // For SCRAP stage
) {}
