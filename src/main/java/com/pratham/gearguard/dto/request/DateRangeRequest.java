package com.pratham.gearguard.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DateRangeRequest(
        @NotNull(message = "Start date is required")
        LocalDateTime startDate,

        @NotNull(message = "End date is required")
        LocalDateTime endDate
) {}
