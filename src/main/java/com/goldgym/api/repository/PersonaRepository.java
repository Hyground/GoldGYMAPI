package com.goldgym.api.repository;

import com.goldgym.api.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
}
