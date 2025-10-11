package com.goldgym.api.controllers;

import com.goldgym.api.dto.response.ClienteResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    // Para la creación, asumimos que todos los autenticados pueden usar el unified endpoint,
    // o que se maneja a través del endpoint unificado con permisos en el PersonaController.
    // Si necesitas restringir este endpoint directo, añade @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.crear(cliente));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.actualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    // Se asume que listar es visible para todos los que gestionan:
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") 
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    // SOLUCIÓN: Permite a ADMINISTRADOR y EMPLEADO obtener los datos para editar
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") 
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }
}