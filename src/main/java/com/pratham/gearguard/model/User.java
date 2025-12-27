package com.pratham.gearguard.model;

import com.pratham.gearguard.model.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole role; // ADMIN, MANAGER, TECHNICIAN, EMPLOYEE

  private String avatar; // URL or path to avatar image

  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  @ManyToMany(mappedBy = "members")
  private Set<MaintenanceTeam> teams = new HashSet<>();

  @OneToMany(mappedBy = "assignedEmployee")
  private List<Equipment> assignedEquipment = new ArrayList<>();

  @OneToMany(mappedBy = "assignedTechnician")
  private List<MaintenanceRequest> assignedRequests = new ArrayList<>();

  private Boolean isActive = true;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}
