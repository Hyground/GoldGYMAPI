package com.goldgym.api.controllers;

import com.goldgym.api.dto.response.UsuarioResponseDTO; // Importar DTO
import com.goldgym.api.entities.Usuario;
import com.goldgym.api.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Crear sigue aceptando Entidad (simplificado, endpoint unificado es mejor)
    // Pero podría devolver DTO para consistencia
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crear(usuario);
        // Mapear a DTO antes de devolver (necesitas el método map en UsuarioService)
        // O hacer que crear devuelva DTO directamente
        return ResponseEntity.ok(usuarioService.obtenerUsuarioDTOPorId(nuevoUsuario.getId())); // Recargar para obtener DTO
    }

    // Actualizar acepta Entidad (debería aceptar DTO Request) y devuelve DTO
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        // La lógica de actualización ahora está en el Service
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(usuarioService.obtenerUsuarioDTOPorId(actualizado.getId())); // Recargar para DTO
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Listar devuelve Lista de DTOs
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // Obtener por ID devuelve DTO
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioDTOPorId(id));
    }

     // Nuevo endpoint para buscar por personaId (usado en frontend para editar Cliente/Empleado)
     @GetMapping("/por-persona/{personaId}")
     public ResponseEntity<UsuarioResponseDTO> obtenerPorPersonaId(@PathVariable Long personaId) {
          return ResponseEntity.ok(usuarioService.obtenerUsuarioDTOPorPersonaId(personaId));
     }
}
