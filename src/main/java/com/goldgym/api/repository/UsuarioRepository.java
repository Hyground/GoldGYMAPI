package com.goldgym.api.repository;

import com.goldgym.api.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query; // Opcional si usas query manual
// import org.springframework.data.repository.query.Param; // Opcional

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    // *** NUEVO: Método para buscar por ID de Persona ***
    Optional<Usuario> findByPersonaId(Long personaId);

    // Alternativa con @Query si la relación es compleja o el nombre no funciona:
    // @Query("SELECT u FROM Usuario u WHERE u.persona.id = :personaId")
    // Optional<Usuario> findByAssociatedPersonaId(@Param("personaId") Long personaId);
}
