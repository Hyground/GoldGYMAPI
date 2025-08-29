package com.goldgym.api.repository;

import com.goldgym.api.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PersonaRepository extends JpaRepository<Persona, UUID> {
  Optional<Persona> findByCorreoIgnoreCase(String correo);
  Optional<Persona> findByTelefono(String telefono);
}
