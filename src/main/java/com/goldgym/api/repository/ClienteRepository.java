package com.goldgym.api.repository;

import com.goldgym.api.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Para query personalizada
import org.springframework.data.repository.query.Param; // Para parámetros

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    long countByActivoTrue(); // Para el dashboard

    Optional<Cliente> findByPersonaId(Long personaId); // Para buscar cliente asociado a persona

    // Método para búsqueda flexible (usado en modal de pagos)
    @Query("SELECT c FROM Cliente c JOIN c.persona p " +
           "WHERE LOWER(p.nombre) LIKE :query " +
           "OR LOWER(p.apellido) LIKE :query " +
           "OR LOWER(c.codigo) LIKE :query " +
           "OR LOWER(p.correo) LIKE :query " +
           "OR CAST(c.id AS string) LIKE :query") // Buscar también por ID como texto
    List<Cliente> buscarPorNombreApellidoCodigoCorreo(@Param("query") String query);

}
