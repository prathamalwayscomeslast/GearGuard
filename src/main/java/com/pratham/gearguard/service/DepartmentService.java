package com.pratham.gearguard.service;

import com.pratham.gearguard.dto.request.CreateDepartmentRequest;
import com.pratham.gearguard.dto.response.DepartmentResponse;
import com.pratham.gearguard.exception.ResourceNotFoundException;
import com.pratham.gearguard.exception.DuplicateResourceException;
import com.pratham.gearguard.model.Department;
import com.pratham.gearguard.model.User;
import com.pratham.gearguard.repository.DepartmentRepository;
import com.pratham.gearguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final UserRepository userRepository;

  public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
    if (departmentRepository.existsByName(request.name())) {
      throw new DuplicateResourceException("Department already exists: " + request.name());
    }

    Department department = new Department();
    department.setName(request.name());
    department.setDescription(request.description());

    if (request.managerId() != null) {
      User manager = userRepository.findById(request.managerId())
              .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
      department.setManager(manager);
    }

    Department savedDepartment = departmentRepository.save(department);
    return mapToResponse(savedDepartment);
  }

  @Transactional(readOnly = true)
  public DepartmentResponse getDepartmentById(Long id) {
    Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    return mapToResponse(department);
  }

  @Transactional(readOnly = true)
  public List<DepartmentResponse> getAllDepartments() {
    return departmentRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  private DepartmentResponse mapToResponse(Department department) {
    return new DepartmentResponse(
            department.getId(),
            department.getName(),
            department.getDescription(),
            department.getManager() != null ? department.getManager().getName() : null,
            department.getManager() != null ? department.getManager().getId() : null
    );
  }
}
