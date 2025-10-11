package com.goldgym.api.controllers;

import com.goldgym.api.dto.response.ClientePagoStatusDTO;
import com.goldgym.api.entities.Pago;
import com.goldgym.api.services.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<Pago> crear(@RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.crear(pago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id, @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.actualizar(id, pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clientes-status")
    // Proteger el endpoint analítico
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<List<ClientePagoStatusDTO>> getClientesEstadoPago() {
        return ResponseEntity.ok(pagoService.getClientesConEstadoPago());
    }

    // SOLUCIÓN: CONSOLIDAMOS LOS MÉTODOS LISTAR EN UNA SOLA DEFINICIÓN.
    @GetMapping // Mapea a GET /api/pagos
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<List<Pago>> listar(@RequestParam(required = false) Long clienteId) {
        if (clienteId != null) {
             // Si se proporciona clienteId, devolvemos el historial (find by clienteId)
             return ResponseEntity.ok(pagoService.obtenerPagosPorCliente(clienteId));
        }
        // Si no se proporciona clienteId, devolvemos todos los pagos (findAll)
        return ResponseEntity.ok(pagoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }
}
