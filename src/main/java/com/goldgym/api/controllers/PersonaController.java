package com.goldgym.api.controllers;

import com.goldgym.api.dto.request.PersonaRequestDTO;
import com.goldgym.api.entities.Persona;
import com.goldgym.api.services.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize; // IMPORT NECESARIO
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    // --- SOLUCIÓN 403: ENDPOINT DE BÚSQUEDA ---
    @GetMapping("/search")
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") // ABRE EL ACCESO PARA EL MODAL DE EMPLEADOS
    public ResponseEntity<List<Persona>> searchPersonas(@RequestParam String name) {
        // NOTA: Asume que PersonaService.searchPersonas(name) existe y está implementado
        return ResponseEntity.ok(personaService.searchPersonas(name)); 
    }
    // ------------------------------------------

    @PostMapping("/unified")
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<String> createUnified(@RequestBody PersonaRequestDTO request) {
        personaService.createUnified(request);
        return ResponseEntity.ok("Entidad creada con éxito");
    }

    @PostMapping
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Persona> crear(@RequestBody Persona persona) {
        return ResponseEntity.ok(personaService.crear(persona));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @RequestBody Persona persona) {
        return ResponseEntity.ok(personaService.actualizar(id, persona));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<List<Persona>> listar() {
        return ResponseEntity.ok(personaService.listar());
    }

    @GetMapping("/{id}")
   // @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Persona> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }
}
