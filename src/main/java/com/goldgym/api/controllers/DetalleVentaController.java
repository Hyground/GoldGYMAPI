package com.goldgym.api.controllers;

import com.goldgym.api.entities.DetalleVenta;
import com.goldgym.api.services.DetalleVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-ventas")
@RequiredArgsConstructor
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    @PostMapping
    public ResponseEntity<DetalleVenta> crear(@RequestBody DetalleVenta detalleVenta) {
        return ResponseEntity.ok(detalleVentaService.crear(detalleVenta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizar(@PathVariable Long id, @RequestBody DetalleVenta detalleVenta) {
        return ResponseEntity.ok(detalleVentaService.actualizar(id, detalleVenta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        detalleVentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listar() {
        return ResponseEntity.ok(detalleVentaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.obtenerPorId(id));
    }
}
