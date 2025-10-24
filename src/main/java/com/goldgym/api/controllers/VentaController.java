package com.goldgym.api.controllers;

import com.goldgym.api.dto.request.VentaRequestDTO;
import com.goldgym.api.dto.response.VentaResponseDTO;
import com.goldgym.api.entities.Venta;
import com.goldgym.api.services.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // IMPORT NECESARIO
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping // <-- ESTE MÉTODO ES EL QUE FALLA CON 403
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") // SOLUCIÓN: Solo Admin y Empleado pueden crear ventas
    public ResponseEntity<Venta> crear(@RequestBody VentaRequestDTO ventaDTO) { 
        // Asumo que el método crear del controller recibe un DTO o la entidad final
        // Debes ajustar la firma si recibe la entidad Venta directamente
        Venta venta = ventaService.crear(ventaDTO); 
        return ResponseEntity.ok(venta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizar(@PathVariable Long id, @RequestBody Venta venta) {
        return ResponseEntity.ok(ventaService.actualizar(id, venta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<List<VentaResponseDTO>> listar() {
        return ResponseEntity.ok(ventaService.listarVentasDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }
}
