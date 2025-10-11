// Archivo: PlanMembresiaController.java

package com.goldgym.api.controllers;

import com.goldgym.api.dto.response.PlanAnaliticasDTO;
import com.goldgym.api.dto.response.PlanAnaliticasDTO;
import com.goldgym.api.entities.PlanMembresia;
import com.goldgym.api.services.PlanMembresiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import necesario
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanMembresiaController {

    private final PlanMembresiaService planService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<PlanMembresia> crear(@RequestBody PlanMembresia plan) {
        return ResponseEntity.ok(planService.crear(plan));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<PlanMembresia> actualizar(@PathVariable Long id, @RequestBody PlanMembresia plan) {
        return ResponseEntity.ok(planService.actualizar(id, plan));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        planService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PlanMembresia>> listar() {
        return ResponseEntity.ok(planService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Para la edición (obtener datos del plan)
    public ResponseEntity<PlanMembresia> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(planService.obtenerPorId(id));
    }

    // NUEVO ENDPOINT PARA ANALÍTICA
    @GetMapping("/analiticas")
    public ResponseEntity<List<PlanAnaliticasDTO>> listarAnaliticas() {
        return ResponseEntity.ok(planService.obtenerAnaliticas());
    }
    
}