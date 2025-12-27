package com.pratham.gearguard.repository;

import com.pratham.gearguard.model.MaintenanceLog;
import com.pratham.gearguard.model.enums.LogAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {

  List<MaintenanceLog> findByRequestId(Long requestId);

  List<MaintenanceLog> findByUserId(Long userId);

  List<MaintenanceLog> findByAction(LogAction action);

  List<MaintenanceLog> findByRequestIdOrderByTimestampDesc(Long requestId);

  List<MaintenanceLog> findByTimestampBetween(
          LocalDateTime startDate,
          LocalDateTime endDate
  );
}
