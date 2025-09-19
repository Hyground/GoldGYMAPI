package com.goldgym.api.repository;

import com.goldgym.api.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    
}
