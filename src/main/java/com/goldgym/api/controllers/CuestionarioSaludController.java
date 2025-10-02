package com.goldgym.api.controllers;

import com.goldgym.api.entities.CuestionarioSalud;
import com.goldgym.api.services.CuestionarioSaludService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuestionarios-salud")
@RequiredArgsConstructor
public class CuestionarioSaludController {

    private final CuestionarioSaludService cuestionarioSaludService;

    @PostMapping
    public ResponseEntity<CuestionarioSalud> crear(@RequestBody CuestionarioSalud cuestionario) {
        return ResponseEntity.ok(cuestionarioSaludService.crear(cuestionario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuestionarioSalud> actualizar(@PathVariable Long id, @RequestBody CuestionarioSalud cuestionario) {
        return ResponseEntity.ok(cuestionarioSaludService.actualizar(id, cuestionario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuestionarioSaludService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CuestionarioSalud>> listar() {
        return ResponseEntity.ok(cuestionarioSaludService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuestionarioSalud> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuestionarioSaludService.obtenerPorId(id));
    }
}
