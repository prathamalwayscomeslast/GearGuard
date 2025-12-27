package com.pratham.gearguard.service;

import com.pratham.gearguard.exception.ResourceNotFoundException;
import com.pratham.gearguard.exception.DuplicateResourceException;
import com.pratham.gearguard.model.User;
import com.pratham.gearguard.model.Department;
import com.pratham.gearguard.model.enums.UserRole;
import com.pratham.gearguard.repository.UserRepository;
import com.pratham.gearguard.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final DepartmentRepository departmentRepository;

  public UserResponse createUser(CreateUserRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new DuplicateResourceException("Email already exists: " + request.email());
    }

    User user = new User();
    user.setName(request.name());
    user.setEmail(request.email());
    user.setPassword(request.password()); // Should be encrypted in production
    user.setRole(request.role());
    user.setPhoneNumber(request.phoneNumber());
    user.setAvatar(request.avatar());

    if (request.departmentId() != null) {
      Department department = departmentRepository.findById(request.departmentId())
              .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
      user.setDepartment(department);
    }

    User savedUser = userRepository.save(user);
    return mapToResponse(savedUser);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    return mapToResponse(user);
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getUsersByRole(UserRole role) {
    return userRepository.findByRole(role).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getUsersByTeam(Long teamId) {
    return userRepository.findByTeamId(teamId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
  }

  public UserResponse updateUser(Long id, UpdateUserRequest request) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    if (request.name() != null) user.setName(request.name());
    if (request.phoneNumber() != null) user.setPhoneNumber(request.phoneNumber());
    if (request.avatar() != null) user.setAvatar(request.avatar());
    if (request.role() != null) user.setRole(request.role());
    if (request.isActive() != null) user.setIsActive(request.isActive());

    if (request.departmentId() != null) {
      Department department = departmentRepository.findById(request.departmentId())
              .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
      user.setDepartment(department);
    }

    User updatedUser = userRepository.save(user);
    return mapToResponse(updatedUser);
  }

  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }

  private UserResponse mapToResponse(User user) {
    return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getPhoneNumber(),
            user.getAvatar(),
            user.getDepartment() != null ? user.getDepartment().getName() : null,
            user.getDepartment() != null ? user.getDepartment().getId() : null,
            user.getIsActive()
    );
  }
}
