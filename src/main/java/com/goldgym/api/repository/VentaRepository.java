package com.goldgym.api.repository;

import com.goldgym.api.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    
}
