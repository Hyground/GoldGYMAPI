package com.goldgym.api.repository;

import com.goldgym.api.entities.Cliente;
import com.goldgym.api.entities.Persona; // Importar Persona
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar Query
import org.springframework.data.repository.query.Param; // Importar Param
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Importar Optional

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Método para buscar Cliente asociado a una Persona (usado en ClienteService)
    Optional<Cliente> findByPersona(Persona persona);

    // Método para búsqueda por query (usado en ClienteService.buscarClientesPorQuery)
    // Busca en nombre/apellido de Persona, código de Cliente o correo de Persona
    @Query("SELECT c FROM Cliente c JOIN c.persona p WHERE " +
           "LOWER(p.nombre) LIKE LOWER(concat('%', :query, '%')) OR " +
           "LOWER(p.apellido) LIKE LOWER(concat('%', :query, '%')) OR " +
           "LOWER(c.codigo) LIKE LOWER(concat('%', :query, '%')) OR " +
           "LOWER(p.correo) LIKE LOWER(concat('%', :query, '%'))")
    List<Cliente> findByNombreOrApellidoOrCodigoOrCorreoContainingIgnoreCase(@Param("query") String query);

    // --- MÉTODO AÑADIDO PARA DashboardEmpleadoController ---
    long countByActivoTrue(); // Contará todos los clientes donde el campo 'activo' sea true

}
