package com.pratham.gearguard.repository;

import com.pratham.gearguard.model.MaintenanceTeam;
import com.pratham.gearguard.model.enums.TeamSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceTeamRepository extends JpaRepository<MaintenanceTeam, Long> {

  Optional<MaintenanceTeam> findByName(String name);

  List<MaintenanceTeam> findBySpecialization(TeamSpecialization specialization);

  List<MaintenanceTeam> findByIsActiveTrue();

  @Query("SELECT t FROM MaintenanceTeam t JOIN t.members m WHERE m.id = :userId")
  List<MaintenanceTeam> findTeamsByMemberId(@Param("userId") Long userId);

  @Query("SELECT t FROM MaintenanceTeam t WHERE t.teamLead.id = :leadId")
  List<MaintenanceTeam> findTeamsByLeadId(@Param("leadId") Long leadId);

  boolean existsByName(String name);
}
