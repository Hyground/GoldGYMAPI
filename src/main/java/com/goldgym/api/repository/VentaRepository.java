package com.goldgym.api.repository;

import com.goldgym.api.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    
}