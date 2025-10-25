package com.goldgym.api.repository;

import com.goldgym.api.entities.Cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    long countByActivoTrue();
    Optional<Cliente> findByPersonaId(Long personaId);
}
