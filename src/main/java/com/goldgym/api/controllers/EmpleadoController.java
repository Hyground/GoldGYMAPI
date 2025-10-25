package com.goldgym.api.controllers;

// --- CAMBIO: Importar EmpleadoResponseDTO ---
import com.goldgym.api.dto.response.EmpleadoResponseDTO;
import com.goldgym.api.entities.Empleado;
import com.goldgym.api.services.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize; // Comentado por ahora
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @PostMapping
    public ResponseEntity<Empleado> crear(@RequestBody Empleado empleado) {
         // Nota: Idealmente, crear también debería usar un DTO de Request y devolver DTO de Response
        return ResponseEntity.ok(empleadoService.crear(empleado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody Empleado empleado) {
        // Nota: Idealmente, actualizar debería devolver EmpleadoResponseDTO
        return ResponseEntity.ok(empleadoService.actualizar(id, empleado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> listar() {
         // Asumiendo que empleadoService.listar() ya devuelve List<EmpleadoResponseDTO>
        return ResponseEntity.ok(empleadoService.listar());
    }

    // --- MÉTODO MODIFICADO ---
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> obtenerPorId(@PathVariable Long id) {
        // Llamar al método del servicio que ahora devuelve EmpleadoResponseDTO
        EmpleadoResponseDTO empleadoDTO = empleadoService.obtenerEmpleadoDTOPorId(id); // Necesitarás crear este método en el Service
        return ResponseEntity.ok(empleadoDTO);
    }
}
