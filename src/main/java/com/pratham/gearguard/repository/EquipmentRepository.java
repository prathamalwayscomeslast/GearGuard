package com.pratham.gearguard.repository;

import com.pratham.gearguard.model.Equipment;
import com.pratham.gearguard.model.enums.EquipmentCategory;
import com.pratham.gearguard.model.enums.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

  Optional<Equipment> findBySerialNumber(String serialNumber);

  List<Equipment> findByDepartmentId(Long departmentId);

  List<Equipment> findByAssignedEmployeeId(Long employeeId);

  List<Equipment> findByCategory(EquipmentCategory category);

  List<Equipment> findByMaintenanceTeamId(Long teamId);

  List<Equipment> findByStatus(EquipmentStatus status);

  List<Equipment> findByIsScrapTrue();

  List<Equipment> findByIsScrapFalse();

  @Query("SELECT e FROM Equipment e WHERE e.department.id = :departmentId AND e.category = :category")
  List<Equipment> findByDepartmentAndCategory(
          @Param("departmentId") Long departmentId,
          @Param("category") EquipmentCategory category
  );

  @Query("SELECT e FROM Equipment e WHERE e.assignedEmployee.id = :employeeId AND e.isScrap = false")
  List<Equipment> findActiveEquipmentByEmployee(@Param("employeeId") Long employeeId);

  boolean existsBySerialNumber(String serialNumber);
}
