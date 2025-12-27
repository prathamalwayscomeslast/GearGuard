package com.pratham.gearguard.model;

import com.pratham.gearguard.model.enums.Priority;
import com.pratham.gearguard.model.enums.RequestStage;
import com.pratham.gearguard.model.enums.RequestType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String subject; // e.g., "Leaking Oil", "Routine Checkup"

  @Column(length = 2000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RequestType type; // CORRECTIVE, PREVENTIVE

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RequestStage stage = RequestStage.NEW;
  // NEW, IN_PROGRESS, REPAIRED, SCRAP

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Priority priority = Priority.MEDIUM;
  // LOW, MEDIUM, HIGH, URGENT

  // Relationships
  @ManyToOne
  @JoinColumn(name = "equipment_id", nullable = false)
  private Equipment equipment;

  @ManyToOne
  @JoinColumn(name = "maintenance_team_id", nullable = false)
  private MaintenanceTeam maintenanceTeam; // Auto-filled from equipment

  @ManyToOne
  @JoinColumn(name = "assigned_technician_id")
  private User assignedTechnician;

  @ManyToOne
  @JoinColumn(name = "created_by_id", nullable = false)
  private User createdBy;

  // Scheduling
  private LocalDateTime scheduledDate; // For preventive maintenance

  private LocalDateTime startedAt; // When moved to IN_PROGRESS

  private LocalDateTime completedAt; // When moved to REPAIRED

  private Integer durationInMinutes; // Time spent on repair

  // Resolution details
  @Column(length = 2000)
  private String resolutionNotes;

  @Column(length = 1000)
  private String scrapReason; // If moved to SCRAP stage

  // Audit fields
  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  // Helper method to check if overdue
  @Transient
  public boolean isOverdue() {
    if (scheduledDate == null || stage == RequestStage.REPAIRED
            || stage == RequestStage.SCRAP) {
      return false;
    }
    return LocalDateTime.now().isAfter(scheduledDate);
  }
}
