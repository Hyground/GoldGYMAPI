package com.goldgym.api.controllers;

import com.goldgym.api.entities.Medicion;
import com.goldgym.api.services.MedicionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mediciones")
@RequiredArgsConstructor
public class MedicionController {

    private final MedicionService medicionService;

    @PostMapping
    public ResponseEntity<Medicion> crear(@RequestBody Medicion medicion) {
        return ResponseEntity.ok(medicionService.crear(medicion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicion> actualizar(@PathVariable Long id, @RequestBody Medicion medicion) {
        return ResponseEntity.ok(medicionService.actualizar(id, medicion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Medicion>> listar() {
        return ResponseEntity.ok(medicionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicion> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicionService.obtenerPorId(id));
    }
}
