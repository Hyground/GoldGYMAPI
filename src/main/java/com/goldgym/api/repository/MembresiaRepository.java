package com.goldgym.api.repository;

import com.goldgym.api.entities.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface MembresiaRepository extends JpaRepository<Membresia, Long> {
    long countByFechaFinBefore(LocalDate fecha);
}
