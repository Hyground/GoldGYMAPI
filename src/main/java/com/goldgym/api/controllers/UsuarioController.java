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

    // Crear sigue recibiendo la Entidad, pero devuelve DTO
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@RequestBody Usuario usuario) {
        // Asumiendo que `crear` ahora devuelve Usuario, lo mapeamos
        Usuario nuevoUsuario = usuarioService.crear(usuario);
        return ResponseEntity.ok(usuarioService.mapUsuarioToDTO(nuevoUsuario)); // Mapear a DTO
    }

    // Actualizar recibe Entidad (o un RequestDTO si prefieres), devuelve DTO
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
         // Asegurarse que el ID en el path coincide con el del body si existe
         if (usuario.getId() != null && !usuario.getId().equals(id)) {
              // Considerar lanzar BadRequestException
              return ResponseEntity.badRequest().build();
         }
         usuario.setId(id); // Asegurar ID correcto
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(usuarioService.mapUsuarioToDTO(actualizado)); // Mapear a DTO
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Listar ahora devuelve DTO
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarDTOs()); // Llamar al nuevo método que devuelve DTOs
    }

    // Obtener por ID ahora devuelve DTO
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerDTOPorId(id)); // Llamar al nuevo método que devuelve DTO
    }

    // Nuevo endpoint para buscar Usuario por Persona ID (usado en edición Cliente/Empleado)
    @GetMapping("/por-persona/{personaId}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorPersonaId(@PathVariable Long personaId) {
        return ResponseEntity.ok(usuarioService.obtenerDTOPorPersonaId(personaId));
    }
}
