package com.goldgym.api.controllers;

import com.goldgym.api.entities.Membresia;
import com.goldgym.api.services.MembresiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/membresias")
@RequiredArgsConstructor
public class MembresiaController {

    private final MembresiaService membresiaService;

    @PostMapping
    public ResponseEntity<Membresia> crear(@RequestBody Membresia membresia) {
        return ResponseEntity.ok(membresiaService.crear(membresia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membresia> actualizar(@PathVariable Long id, @RequestBody Membresia membresia) {
        return ResponseEntity.ok(membresiaService.actualizar(id, membresia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        membresiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Membresia>> listar() {
        return ResponseEntity.ok(membresiaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membresia> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(membresiaService.obtenerPorId(id));
    }
}
