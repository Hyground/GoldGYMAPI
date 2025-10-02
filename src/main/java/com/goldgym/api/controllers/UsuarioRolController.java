package com.goldgym.api.controllers;

import com.goldgym.api.entities.UsuarioRol;
import com.goldgym.api.services.UsuarioRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario-roles")
@RequiredArgsConstructor
public class UsuarioRolController {

    private final UsuarioRolService usuarioRolService;

    @PostMapping
    public ResponseEntity<UsuarioRol> crear(@RequestBody UsuarioRol usuarioRol) {
        return ResponseEntity.ok(usuarioRolService.crear(usuarioRol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRol> actualizar(@PathVariable Long id, @RequestBody UsuarioRol usuarioRol) {
        return ResponseEntity.ok(usuarioRolService.actualizar(id, usuarioRol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioRolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioRol>> listar() {
        return ResponseEntity.ok(usuarioRolService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRol> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioRolService.obtenerPorId(id));
    }
}
