package com.goldgym.api.repository;

import com.goldgym.api.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicionRepository extends JpaRepository<Medicion, Long> {
    
}