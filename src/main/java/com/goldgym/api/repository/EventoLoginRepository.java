package com.goldgym.api.repository;

import com.goldgym.api.entities.EventoLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoLoginRepository extends JpaRepository<EventoLogin, Long> {
    
}
