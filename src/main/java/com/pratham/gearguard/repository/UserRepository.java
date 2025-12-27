package com.pratham.gearguard.repository;

import com.pratham.gearguard.model.User;
import com.pratham.gearguard.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  List<User> findByRole(UserRole role);

  List<User> findByDepartmentId(Long departmentId);

  List<User> findByIsActiveTrue();

  @Query("SELECT u FROM User u JOIN u.teams t WHERE t.id = :teamId")
  List<User> findByTeamId(@Param("teamId") Long teamId);

  boolean existsByEmail(String email);
}
