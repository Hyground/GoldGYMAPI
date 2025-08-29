package com.goldgym.api.repository;

import com.goldgym.api.domain.PlanMembresia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PlanMembresiaRepository extends JpaRepository<PlanMembresia, UUID> {
  boolean existsByNombreIgnoreCase(String nombre);
}
