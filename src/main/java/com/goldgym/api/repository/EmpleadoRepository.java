package com.goldgym.api.repository;

import com.goldgym.api.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
}
