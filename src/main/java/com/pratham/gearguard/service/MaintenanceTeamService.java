package com.pratham.gearguard.service;

import com.pratham.gearguard.dto.request.CreateTeamRequest;
import com.pratham.gearguard.dto.request.UpdateTeamRequest;
import com.pratham.gearguard.dto.response.TeamResponse;
import com.pratham.gearguard.dto.response.UserResponse;
import com.pratham.gearguard.exception.ResourceNotFoundException;
import com.pratham.gearguard.exception.DuplicateResourceException;
import com.pratham.gearguard.model.MaintenanceTeam;
import com.pratham.gearguard.model.User;
import com.pratham.gearguard.repository.MaintenanceTeamRepository;
import com.pratham.gearguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceTeamService {

  private final MaintenanceTeamRepository teamRepository;
  private final UserRepository userRepository;

  public TeamResponse createTeam(CreateTeamRequest request) {
    if (teamRepository.existsByName(request.name())) {
      throw new DuplicateResourceException("Team already exists: " + request.name());
    }

    MaintenanceTeam team = new MaintenanceTeam();
    team.setName(request.name());
    team.setDescription(request.description());
    team.setSpecialization(request.specialization());

    // Add team members
    if (request.memberIds() != null && !request.memberIds().isEmpty()) {
      Set<User> members = new HashSet<>();
      for (Long memberId : request.memberIds()) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + memberId));
        members.add(user);
      }
      team.setMembers(members);
    }

    // Set team lead
    if (request.teamLeadId() != null) {
      User teamLead = userRepository.findById(request.teamLeadId())
              .orElseThrow(() -> new ResourceNotFoundException("Team lead not found"));
      team.setTeamLead(teamLead);
    }

    MaintenanceTeam savedTeam = teamRepository.save(team);
    return mapToResponse(savedTeam);
  }

  @Transactional(readOnly = true)
  public TeamResponse getTeamById(Long id) {
    MaintenanceTeam team = teamRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    return mapToResponse(team);
  }

  @Transactional(readOnly = true)
  public List<TeamResponse> getAllTeams() {
    return teamRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<TeamResponse> getActiveTeams() {
    return teamRepository.findByIsActiveTrue().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  public TeamResponse addMemberToTeam(Long teamId, Long userId) {
    MaintenanceTeam team = teamRepository.findById(teamId)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    team.getMembers().add(user);
    MaintenanceTeam updatedTeam = teamRepository.save(team);
    return mapToResponse(updatedTeam);
  }

  public TeamResponse removeMemberFromTeam(Long teamId, Long userId) {
    MaintenanceTeam team = teamRepository.findById(teamId)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

    team.getMembers().removeIf(member -> member.getId().equals(userId));
    MaintenanceTeam updatedTeam = teamRepository.save(team);
    return mapToResponse(updatedTeam);
  }

  public TeamResponse updateTeam(Long id, UpdateTeamRequest request) {
    MaintenanceTeam team = teamRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

    if (request.name() != null) team.setName(request.name());
    if (request.description() != null) team.setDescription(request.description());
    if (request.specialization() != null) team.setSpecialization(request.specialization());
    if (request.isActive() != null) team.setIsActive(request.isActive());

    if (request.teamLeadId() != null) {
      User teamLead = userRepository.findById(request.teamLeadId())
              .orElseThrow(() -> new ResourceNotFoundException("Team lead not found"));
      team.setTeamLead(teamLead);
    }

    MaintenanceTeam updatedTeam = teamRepository.save(team);
    return mapToResponse(updatedTeam);
  }

  public void deleteTeam(Long id) {
    if (!teamRepository.existsById(id)) {
      throw new ResourceNotFoundException("Team not found with id: " + id);
    }
    teamRepository.deleteById(id);
  }

  private TeamResponse mapToResponse(MaintenanceTeam team) {
    List<UserResponse> memberResponses = team.getMembers().stream()
            .map(member -> new UserResponse(
                    member.getId(),
                    member.getName(),
                    member.getEmail(),
                    member.getRole(),
                    member.getPhoneNumber(),
                    member.getAvatar(),
                    null,
                    null,
                    member.getIsActive()
            ))
            .collect(Collectors.toList());

    return new TeamResponse(
            team.getId(),
            team.getName(),
            team.getDescription(),
            team.getSpecialization(),
            team.getTeamLead() != null ? team.getTeamLead().getName() : null,
            team.getTeamLead() != null ? team.getTeamLead().getId() : null,
            memberResponses,
            team.getIsActive()
    );
  }
}
