package com.pratham.gearguard.model;

import com.pratham.gearguard.model.enums.TeamSpecialization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "maintenance_teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceTeam {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name; // Mechanics, Electricians, IT Support, HVAC Specialists

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  private TeamSpecialization specialization;
  // MECHANICAL, ELECTRICAL, IT, HVAC, GENERAL

  @ManyToMany
  @JoinTable(
          name = "team_members",
          joinColumns = @JoinColumn(name = "team_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private Set<User> members = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "team_lead_id")
  private User teamLead;

  @OneToMany(mappedBy = "maintenanceTeam")
  private List<Equipment> equipment = new ArrayList<>();

  @OneToMany(mappedBy = "maintenanceTeam")
  private List<MaintenanceRequest> maintenanceRequests = new ArrayList<>();

  private Boolean isActive = true;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}
