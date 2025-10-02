package com.goldgym.api.controllers;

import com.goldgym.api.entities.PlanMembresia;
import com.goldgym.api.services.PlanMembresiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanMembresiaController {

    private final PlanMembresiaService planService;

    @PostMapping
    public ResponseEntity<PlanMembresia> crear(@RequestBody PlanMembresia plan) {
        return ResponseEntity.ok(planService.crear(plan));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanMembresia> actualizar(@PathVariable Long id, @RequestBody PlanMembresia plan) {
        return ResponseEntity.ok(planService.actualizar(id, plan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        planService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PlanMembresia>> listar() {
        return ResponseEntity.ok(planService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanMembresia> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(planService.obtenerPorId(id));
    }
}
