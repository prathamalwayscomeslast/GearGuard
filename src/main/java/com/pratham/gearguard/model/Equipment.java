package com.pratham.gearguard.model;

import com.pratham.gearguard.model.enums.EquipmentCategory;
import com.pratham.gearguard.model.enums.EquipmentStatus;
import com.pratham.gearguard.model.enums.RequestStage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.persistence.CascadeType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String serialNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EquipmentCategory category;
  // PRODUCTION_MACHINERY, IT_EQUIPMENT, VEHICLE, HVAC, ELECTRICAL, etc.

  @Column(nullable = false)
  private String location; // Physical location

  @Column(nullable = false)
  private LocalDate purchaseDate;

  private LocalDate warrantyExpiryDate;

  @Column(length = 1000)
  private String specifications; // Technical details

  // Ownership tracking
  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  @ManyToOne
  @JoinColumn(name = "assigned_employee_id")
  private User assignedEmployee;

  // Maintenance responsibility
  @ManyToOne
  @JoinColumn(name = "maintenance_team_id", nullable = false)
  private MaintenanceTeam maintenanceTeam;

  @ManyToOne
  @JoinColumn(name = "default_technician_id", nullable = false)
  private User defaultTechnician;

  // Status tracking
  @Column(nullable = false)
  private Boolean isScrap = false;

  @Enumerated(EnumType.STRING)
  private EquipmentStatus status = EquipmentStatus.OPERATIONAL;
  // OPERATIONAL, UNDER_MAINTENANCE, OUT_OF_SERVICE, SCRAPPED

  @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
  private List<MaintenanceRequest> maintenanceRequests = new ArrayList<>();

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  // Helper method for smart button count
  @Transient
  public long getOpenRequestsCount() {
    return maintenanceRequests.stream()
            .filter(req -> req.getStage() != RequestStage.REPAIRED
                    && req.getStage() != RequestStage.SCRAP)
            .count();
  }
}
