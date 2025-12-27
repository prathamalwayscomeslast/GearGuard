package com.pratham.gearguard.service;

import com.pratham.gearguard.dto.request.CreateEquipmentRequest;
import com.pratham.gearguard.dto.request.UpdateEquipmentRequest;
import com.pratham.gearguard.dto.response.EquipmentResponse;
import com.pratham.gearguard.exception.ResourceNotFoundException;
import com.pratham.gearguard.exception.DuplicateResourceException;
import com.pratham.gearguard.model.*;
import com.pratham.gearguard.model.enums.EquipmentCategory;
import com.pratham.gearguard.model.enums.EquipmentStatus;
import com.pratham.gearguard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentService {

  private final EquipmentRepository equipmentRepository;
  private final DepartmentRepository departmentRepository;
  private final UserRepository userRepository;
  private final MaintenanceTeamRepository maintenanceTeamRepository;
  private final MaintenanceRequestRepository maintenanceRequestRepository;

  public EquipmentResponse createEquipment(CreateEquipmentRequest request) {
    if (equipmentRepository.existsBySerialNumber(request.serialNumber())) {
      throw new DuplicateResourceException("Equipment with serial number already exists: "
              + request.serialNumber());
    }

    Equipment equipment = new Equipment();
    equipment.setName(request.name());
    equipment.setSerialNumber(request.serialNumber());
    equipment.setCategory(request.category());
    equipment.setLocation(request.location());
    equipment.setPurchaseDate(request.purchaseDate());
    equipment.setWarrantyExpiryDate(request.warrantyExpiryDate());
    equipment.setSpecifications(request.specifications());

    // Set department if provided
    if (request.departmentId() != null) {
      Department department = departmentRepository.findById(request.departmentId())
              .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
      equipment.setDepartment(department);
    }

    // Set assigned employee if provided
    if (request.assignedEmployeeId() != null) {
      User employee = userRepository.findById(request.assignedEmployeeId())
              .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
      equipment.setAssignedEmployee(employee);
    }

    // Set maintenance team (required)
    MaintenanceTeam team = maintenanceTeamRepository.findById(request.maintenanceTeamId())
            .orElseThrow(() -> new ResourceNotFoundException("Maintenance team not found"));
    equipment.setMaintenanceTeam(team);

    // Set default technician (required)
    User technician = userRepository.findById(request.defaultTechnicianId())
            .orElseThrow(() -> new ResourceNotFoundException("Default technician not found"));
    equipment.setDefaultTechnician(technician);

    Equipment savedEquipment = equipmentRepository.save(equipment);
    return mapToResponse(savedEquipment);
  }

  @Transactional(readOnly = true)
  public EquipmentResponse getEquipmentById(Long id) {
    Equipment equipment = equipmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
    return mapToResponse(equipment);
  }

  @Transactional(readOnly = true)
  public List<EquipmentResponse> getAllEquipment() {
    return equipmentRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<EquipmentResponse> getEquipmentByDepartment(Long departmentId) {
    return equipmentRepository.findByDepartmentId(departmentId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<EquipmentResponse> getEquipmentByEmployee(Long employeeId) {
    return equipmentRepository.findByAssignedEmployeeId(employeeId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<EquipmentResponse> getEquipmentByCategory(EquipmentCategory category) {
    return equipmentRepository.findByCategory(category).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  public EquipmentResponse updateEquipment(Long id, UpdateEquipmentRequest request) {
    Equipment equipment = equipmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

    if (request.name() != null) equipment.setName(request.name());
    if (request.location() != null) equipment.setLocation(request.location());
    if (request.specifications() != null) equipment.setSpecifications(request.specifications());
    if (request.status() != null) equipment.setStatus(request.status());

    if (request.departmentId() != null) {
      Department department = departmentRepository.findById(request.departmentId())
              .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
      equipment.setDepartment(department);
    }

    if (request.assignedEmployeeId() != null) {
      User employee = userRepository.findById(request.assignedEmployeeId())
              .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
      equipment.setAssignedEmployee(employee);
    }

    Equipment updatedEquipment = equipmentRepository.save(equipment);
    return mapToResponse(updatedEquipment);
  }

  public void deleteEquipment(Long id) {
    if (!equipmentRepository.existsById(id)) {
      throw new ResourceNotFoundException("Equipment not found with id: " + id);
    }
    equipmentRepository.deleteById(id);
  }

  // Smart button feature - get open requests count
  @Transactional(readOnly = true)
  public Long getOpenRequestsCount(Long equipmentId) {
    if (!equipmentRepository.existsById(equipmentId)) {
      throw new ResourceNotFoundException("Equipment not found with id: " + equipmentId);
    }
    return maintenanceRequestRepository.countOpenRequestsByEquipment(equipmentId);
  }

  private EquipmentResponse mapToResponse(Equipment equipment) {
    Long openRequestsCount = maintenanceRequestRepository
            .countOpenRequestsByEquipment(equipment.getId());

    return new EquipmentResponse(
            equipment.getId(),
            equipment.getName(),
            equipment.getSerialNumber(),
            equipment.getCategory(),
            equipment.getLocation(),
            equipment.getPurchaseDate(),
            equipment.getWarrantyExpiryDate(),
            equipment.getSpecifications(),
            equipment.getDepartment() != null ? equipment.getDepartment().getName() : null,
            equipment.getDepartment() != null ? equipment.getDepartment().getId() : null,
            equipment.getAssignedEmployee() != null ? equipment.getAssignedEmployee().getName() : null,
            equipment.getAssignedEmployee() != null ? equipment.getAssignedEmployee().getId() : null,
            equipment.getMaintenanceTeam().getName(),
            equipment.getMaintenanceTeam().getId(),
            equipment.getDefaultTechnician().getName(),
            equipment.getDefaultTechnician().getId(),
            equipment.getStatus(),
            equipment.getIsScrap(),
            openRequestsCount
    );
  }
}
