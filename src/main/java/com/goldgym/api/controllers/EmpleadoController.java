package com.goldgym.api.controllers;

import com.goldgym.api.dto.response.EmpleadoResponseDTO;
import com.goldgym.api.entities.Empleado;
import com.goldgym.api.services.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import necesario
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Solo Admin puede crear nuevos empleados
    public ResponseEntity<Empleado> crear(@RequestBody Empleado empleado) {
        return ResponseEntity.ok(empleadoService.crear(empleado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") // Admin o el propio Empleado pueden actualizar
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody Empleado empleado) {
        return ResponseEntity.ok(empleadoService.actualizar(id, empleado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Solo Admin puede eliminar
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") // Listar empleados
    public ResponseEntity<List<EmpleadoResponseDTO>> listar() {
        return ResponseEntity.ok(empleadoService.listar());
    }

    @GetMapping("/{id}")
    // SOLUCIÓN: Permite a ADMINISTRADOR y EMPLEADO obtener los datos para editar
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") 
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.obtenerPorId(id));
    }
}