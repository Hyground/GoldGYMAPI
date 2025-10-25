package com.goldgym.api.repository;

import com.goldgym.api.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByPersonaId(Long personaId); // Para buscar empleado asociado a persona
}
