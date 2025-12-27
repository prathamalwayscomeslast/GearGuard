package com.pratham.gearguard.service;

import com.pratham.gearguard.dto.request.CreateMaintenanceRequestRequest;
import com.pratham.gearguard.dto.request.UpdateRequestStageRequest;
import com.pratham.gearguard.dto.request.AssignTechnicianRequest;
import com.pratham.gearguard.dto.response.MaintenanceRequestResponse;
import com.pratham.gearguard.dto.response.KanbanBoardResponse;
import com.pratham.gearguard.exception.ResourceNotFoundException;
import com.pratham.gearguard.exception.BusinessLogicException;
import com.pratham.gearguard.model.*;
import com.pratham.gearguard.model.enums.LogAction;
import com.pratham.gearguard.model.enums.RequestStage;
import com.pratham.gearguard.model.enums.EquipmentStatus;
import com.pratham.gearguard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceRequestService {

  private final MaintenanceRequestRepository requestRepository;
  private final EquipmentRepository equipmentRepository;
  private final UserRepository userRepository;
  private final MaintenanceLogRepository logRepository;

  // Flow 1: The Breakdown - Auto-fill logic
  public MaintenanceRequestResponse createRequest(CreateMaintenanceRequestRequest request, Long currentUserId) {
    // Fetch equipment
    Equipment equipment = equipmentRepository.findById(request.equipmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

    // Fetch creator
    User creator = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    MaintenanceRequest maintenanceRequest = new MaintenanceRequest();
    maintenanceRequest.setSubject(request.subject());
    maintenanceRequest.setDescription(request.description());
    maintenanceRequest.setType(request.type());
    maintenanceRequest.setPriority(request.priority());
    maintenanceRequest.setEquipment(equipment);
    maintenanceRequest.setCreatedBy(creator);

    // AUTO-FILL LOGIC: Fetch team and category from equipment
    maintenanceRequest.setMaintenanceTeam(equipment.getMaintenanceTeam());

    // Request starts in NEW stage
    maintenanceRequest.setStage(RequestStage.NEW);

    // Set scheduled date if provided (for preventive maintenance)
    if (request.scheduledDate() != null) {
      maintenanceRequest.setScheduledDate(request.scheduledDate());
    }

    MaintenanceRequest savedRequest = requestRepository.save(maintenanceRequest);

    // Log creation
    createLog(savedRequest, creator, LogAction.CREATED, null, "Request created");

    return mapToResponse(savedRequest);
  }

  @Transactional(readOnly = true)
  public MaintenanceRequestResponse getRequestById(Long id) {
    MaintenanceRequest request = requestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));
    return mapToResponse(request);
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponse> getAllRequests() {
    return requestRepository.findAllOrderedByPriorityAndDate().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponse> getRequestsByEquipment(Long equipmentId) {
    return requestRepository.findByEquipmentId(equipmentId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponse> getOpenRequestsByEquipment(Long equipmentId) {
    return requestRepository.findOpenRequestsByEquipment(equipmentId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponse> getRequestsByTeam(Long teamId) {
    return requestRepository.findByMaintenanceTeamId(teamId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  // Assignment logic - Flow 1 Step 4
  public MaintenanceRequestResponse assignTechnician(Long requestId, AssignTechnicianRequest request, Long currentUserId) {
    MaintenanceRequest maintenanceRequest = requestRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

    User technician = userRepository.findById(request.technicianId())
            .orElseThrow(() -> new ResourceNotFoundException("Technician not found"));

    User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

    // Verify technician is member of the maintenance team
    if (!maintenanceRequest.getMaintenanceTeam().getMembers().contains(technician)) {
      throw new BusinessLogicException("Technician is not a member of the assigned team");
    }

    maintenanceRequest.setAssignedTechnician(technician);
    MaintenanceRequest updatedRequest = requestRepository.save(maintenanceRequest);

    // Log assignment
    createLog(updatedRequest, currentUser, LogAction.ASSIGNED, null,
            "Assigned to " + technician.getName());

    return mapToResponse(updatedRequest);
  }

  // Stage update - Flow 1 Steps 5 & 6 (Drag & Drop in Kanban)
  public MaintenanceRequestResponse updateStage(Long requestId, UpdateRequestStageRequest request, Long currentUserId) {
    MaintenanceRequest maintenanceRequest = requestRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

    User currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

    RequestStage oldStage = maintenanceRequest.getStage();
    RequestStage newStage = request.stage();

    // Business logic for stage transitions
    if (newStage == RequestStage.IN_PROGRESS) {
      if (maintenanceRequest.getAssignedTechnician() == null) {
        throw new BusinessLogicException("Cannot move to IN_PROGRESS without assigned technician");
      }
      maintenanceRequest.setStartedAt(LocalDateTime.now());
      maintenanceRequest.getEquipment().setStatus(EquipmentStatus.UNDER_MAINTENANCE);
    }

    if (newStage == RequestStage.REPAIRED) {
      if (request.durationInMinutes() != null) {
        maintenanceRequest.setDurationInMinutes(request.durationInMinutes());
      }
      if (request.resolutionNotes() != null) {
        maintenanceRequest.setResolutionNotes(request.resolutionNotes());
      }
      maintenanceRequest.setCompletedAt(LocalDateTime.now());
      maintenanceRequest.getEquipment().setStatus(EquipmentStatus.OPERATIONAL);
    }

    // Scrap logic
    if (newStage == RequestStage.SCRAP) {
      if (request.scrapReason() != null) {
        maintenanceRequest.setScrapReason(request.scrapReason());
      }
      maintenanceRequest.setCompletedAt(LocalDateTime.now());
      maintenanceRequest.getEquipment().setIsScrap(true);
      maintenanceRequest.getEquipment().setStatus(EquipmentStatus.SCRAPPED);
    }

    maintenanceRequest.setStage(newStage);
    MaintenanceRequest updatedRequest = requestRepository.save(maintenanceRequest);

    // Log stage change
    createLog(updatedRequest, currentUser, LogAction.STAGE_CHANGED,
            oldStage.toString(), newStage.toString());

    return mapToResponse(updatedRequest);
  }

  // Kanban board view
  @Transactional(readOnly = true)
  public KanbanBoardResponse getKanbanBoard() {
    List<MaintenanceRequest> allRequests = requestRepository.findAllOrderedByPriorityAndDate();

    Map<RequestStage, List<MaintenanceRequestResponse>> groupedByStage = allRequests.stream()
            .collect(Collectors.groupingBy(
                    MaintenanceRequest::getStage,
                    Collectors.mapping(this::mapToResponse, Collectors.toList())
            ));

    return new KanbanBoardResponse(groupedByStage);
  }

  // Calendar view - Flow 2: Preventive maintenance
  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponse> getCalendarView(LocalDateTime startDate, LocalDateTime endDate) {
    return requestRepository.findByScheduledDateBetween(startDate, endDate).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponse> getPreventiveMaintenanceInRange(
          LocalDateTime startDate, LocalDateTime endDate) {
    return requestRepository.findPreventiveMaintenanceInDateRange(startDate, endDate).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MaintenanceRequestResponse> getOverdueRequests() {
    return requestRepository.findOverdueRequests(LocalDateTime.now()).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  private void createLog(MaintenanceRequest request, User user, LogAction action,
                         String previousValue, String newValue) {
    MaintenanceLog log = new MaintenanceLog();
    log.setRequest(request);
    log.setUser(user);
    log.setAction(action);
    log.setPreviousValue(previousValue);
    log.setNewValue(newValue);
    logRepository.save(log);
  }

  private MaintenanceRequestResponse mapToResponse(MaintenanceRequest request) {
    return new MaintenanceRequestResponse(
            request.getId(),
            request.getSubject(),
            request.getDescription(),
            request.getType(),
            request.getStage(),
            request.getPriority(),
            request.getEquipment().getName(),
            request.getEquipment().getId(),
            request.getEquipment().getCategory(),
            request.getMaintenanceTeam().getName(),
            request.getMaintenanceTeam().getId(),
            request.getAssignedTechnician() != null ? request.getAssignedTechnician().getName() : null,
            request.getAssignedTechnician() != null ? request.getAssignedTechnician().getId() : null,
            request.getAssignedTechnician() != null ? request.getAssignedTechnician().getAvatar() : null,
            request.getCreatedBy().getName(),
            request.getCreatedBy().getId(),
            request.getScheduledDate(),
            request.getStartedAt(),
            request.getCompletedAt(),
            request.getDurationInMinutes(),
            request.getResolutionNotes(),
            request.getScrapReason(),
            request.getCreatedAt(),
            request.getUpdatedAt(),
            request.isOverdue()
    );
  }
}
