package com.pratham.gearguard.model;

import com.pratham.gearguard.model.enums.LogAction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "request_id", nullable = false)
  private MaintenanceRequest request;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  private LogAction action;
  // CREATED, ASSIGNED, STAGE_CHANGED, COMMENTED, COMPLETED, SCRAPPED

  private String previousValue;
  private String newValue;

  @Column(length = 1000)
  private String comment;

  @CreatedDate
  private LocalDateTime timestamp;
}
