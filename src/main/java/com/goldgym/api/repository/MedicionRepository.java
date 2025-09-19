package com.goldgym.api.repository;

import com.goldgym.api.entities.Medicion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicionRepository extends JpaRepository<Medicion, Long> {
    
}
