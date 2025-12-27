package com.pratham.gearguard.service;

import com.pratham.gearguard.dto.response.TeamStatisticsResponse;
import com.pratham.gearguard.dto.response.CategoryStatisticsResponse;
import com.pratham.gearguard.model.enums.EquipmentCategory;
import com.pratham.gearguard.repository.MaintenanceRequestRepository;
import com.pratham.gearguard.repository.MaintenanceTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

  private final MaintenanceRequestRepository requestRepository;
  private final MaintenanceTeamRepository teamRepository;

  public List<TeamStatisticsResponse> getRequestsByTeam() {
    List<Object[]> results = requestRepository.countRequestsByTeam();

    return results.stream()
            .map(result -> {
              Long teamId = (Long) result[0];
              Long count = (Long) result[1];
              String teamName = teamRepository.findById(teamId)
                      .map(team -> team.getName())
                      .orElse("Unknown");

              return new TeamStatisticsResponse(teamId, teamName, count);
            })
            .collect(Collectors.toList());
  }

  public List<CategoryStatisticsResponse> getRequestsByEquipmentCategory() {
    List<Object[]> results = requestRepository.countRequestsByEquipmentCategory();

    return results.stream()
            .map(result -> {
              EquipmentCategory category = (EquipmentCategory) result[0];
              Long count = (Long) result[1];

              return new CategoryStatisticsResponse(category, count);
            })
            .collect(Collectors.toList());
  }
}
