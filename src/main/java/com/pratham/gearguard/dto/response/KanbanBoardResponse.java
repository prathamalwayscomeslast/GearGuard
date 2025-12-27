package com.pratham.gearguard.dto.response;

import com.pratham.gearguard.model.enums.RequestStage;

import java.util.List;
import java.util.Map;

public record KanbanBoardResponse(
        Map<RequestStage, List<MaintenanceRequestResponse>> stages
) {}
