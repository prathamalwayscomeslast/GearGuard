package com.pratham.gearguard.repository;

import com.pratham.gearguard.model.MaintenanceRequest;
import com.pratham.gearguard.model.enums.RequestStage;
import com.pratham.gearguard.model.enums.RequestType;
import com.pratham.gearguard.model.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {

  List<MaintenanceRequest> findByEquipmentId(Long equipmentId);

  List<MaintenanceRequest> findByMaintenanceTeamId(Long teamId);

  List<MaintenanceRequest> findByAssignedTechnicianId(Long technicianId);

  List<MaintenanceRequest> findByCreatedById(Long userId);

  List<MaintenanceRequest> findByStage(RequestStage stage);

  List<MaintenanceRequest> findByType(RequestType type);

  List<MaintenanceRequest> findByPriority(Priority priority);

  // For Kanban board - group by stage
  @Query("SELECT r FROM MaintenanceRequest r ORDER BY r.priority DESC, r.createdAt ASC")
  List<MaintenanceRequest> findAllOrderedByPriorityAndDate();

  // For smart button - open requests for specific equipment
  @Query("SELECT r FROM MaintenanceRequest r WHERE r.equipment.id = :equipmentId " +
          "AND r.stage NOT IN ('REPAIRED', 'SCRAP')")
  List<MaintenanceRequest> findOpenRequestsByEquipment(@Param("equipmentId") Long equipmentId);

  // Count open requests for smart button badge
  @Query("SELECT COUNT(r) FROM MaintenanceRequest r WHERE r.equipment.id = :equipmentId " +
          "AND r.stage NOT IN ('REPAIRED', 'SCRAP')")
  Long countOpenRequestsByEquipment(@Param("equipmentId") Long equipmentId);

  // For calendar view - preventive maintenance in date range
  @Query("SELECT r FROM MaintenanceRequest r WHERE r.type = 'PREVENTIVE' " +
          "AND r.scheduledDate BETWEEN :startDate AND :endDate")
  List<MaintenanceRequest> findPreventiveMaintenanceInDateRange(
          @Param("startDate") LocalDateTime startDate,
          @Param("endDate") LocalDateTime endDate
  );

  // All requests in date range (for calendar)
  List<MaintenanceRequest> findByScheduledDateBetween(
          LocalDateTime startDate,
          LocalDateTime endDate
  );

  // Overdue requests
  @Query("SELECT r FROM MaintenanceRequest r WHERE r.scheduledDate < :now " +
          "AND r.stage NOT IN ('REPAIRED', 'SCRAP')")
  List<MaintenanceRequest> findOverdueRequests(@Param("now") LocalDateTime now);

  // Team workload - requests by team and stage
  @Query("SELECT r FROM MaintenanceRequest r WHERE r.maintenanceTeam.id = :teamId " +
          "AND r.stage = :stage")
  List<MaintenanceRequest> findByTeamAndStage(
          @Param("teamId") Long teamId,
          @Param("stage") RequestStage stage
  );

  // For reports - count by team
  @Query("SELECT r.maintenanceTeam.id, COUNT(r) FROM MaintenanceRequest r " +
          "GROUP BY r.maintenanceTeam.id")
  List<Object[]> countRequestsByTeam();

  // For reports - count by equipment category
  @Query("SELECT r.equipment.category, COUNT(r) FROM MaintenanceRequest r " +
          "GROUP BY r.equipment.category")
  List<Object[]> countRequestsByEquipmentCategory();

  // For reports - count by stage for a team
  @Query("SELECT r.stage, COUNT(r) FROM MaintenanceRequest r " +
          "WHERE r.maintenanceTeam.id = :teamId GROUP BY r.stage")
  List<Object[]> countRequestsByStageForTeam(@Param("teamId") Long teamId);

  // Unassigned requests for a team
  @Query("SELECT r FROM MaintenanceRequest r WHERE r.maintenanceTeam.id = :teamId " +
          "AND r.assignedTechnician IS NULL AND r.stage = 'NEW'")
  List<MaintenanceRequest> findUnassignedRequestsForTeam(@Param("teamId") Long teamId);
}
