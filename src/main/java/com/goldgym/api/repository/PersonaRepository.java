package com.goldgym.api.repository;

import com.goldgym.api.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Importar List

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    // Método para buscar por nombre, apellido o correo (ignorando mayúsculas/minúsculas)
    List<Persona> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCorreoContainingIgnoreCase(
        String nombre, String apellido, String correo
    );
}
