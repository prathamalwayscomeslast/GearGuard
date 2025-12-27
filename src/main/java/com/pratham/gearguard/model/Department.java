package com.pratham.gearguard.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name; // Production, IT, HR, Finance, etc.

  private String description;

  @ManyToOne
  @JoinColumn(name = "manager_id")
  private User manager;

  @OneToMany(mappedBy = "department")
  private List<Equipment> equipment = new ArrayList<>();

  @OneToMany(mappedBy = "department")
  private List<User> employees = new ArrayList<>();

  @CreatedDate
  private LocalDateTime createdAt;
}
