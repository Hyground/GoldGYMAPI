package com.goldgym.api.controllers;

import com.goldgym.api.dto.request.PersonaRequestDTO;
import com.goldgym.api.entities.Persona;
import com.goldgym.api.services.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @PostMapping("/unified")
    public ResponseEntity<String> createUnified(@RequestBody PersonaRequestDTO request) {
        personaService.createUnified(request);
        return ResponseEntity.ok("Entidad creada con éxito");
    }

    @PostMapping
    public ResponseEntity<Persona> crear(@RequestBody Persona persona) {
        return ResponseEntity.ok(personaService.crear(persona));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @RequestBody Persona persona) {
        return ResponseEntity.ok(personaService.actualizar(id, persona));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Persona>> listar() {
        return ResponseEntity.ok(personaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }
}