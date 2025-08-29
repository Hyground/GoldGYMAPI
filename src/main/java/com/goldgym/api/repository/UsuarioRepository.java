package com.goldgym.api.repository;

import com.goldgym.api.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
  Optional<Usuario> findByUsername(String username);
  Optional<Usuario> findByPersona_CorreoIgnoreCase(String correo);
  Optional<Usuario> findByPersona_Telefono(String telefono);
}
