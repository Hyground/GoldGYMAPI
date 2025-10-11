package com.goldgym.api.repository;

import com.goldgym.api.entities.PlanMembresia;
import com.goldgym.api.dto.response.PlanAnaliticasDTO; // Importar el nuevo DTO
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanMembresiaRepository extends JpaRepository<PlanMembresia, Long> {
    
    // Nueva consulta para obtener analíticas de clientes por plan
    @Query("SELECT new com.goldgym.api.dto.response.PlanAnaliticasDTO(" +
           "p.id, p.nombre, p.precio, p.duracionDias, p.descripcion, COUNT(m.id)) " +
           "FROM PlanMembresia p LEFT JOIN Membresia m ON p.id = m.plan.id " +
           "GROUP BY p.id, p.nombre, p.precio, p.duracionDias, p.descripcion")
    List<PlanAnaliticasDTO> findPlanAnaliticas();
}