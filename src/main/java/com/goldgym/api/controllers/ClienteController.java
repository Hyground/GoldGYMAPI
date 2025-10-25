package com.goldgym.api.controllers;

import com.goldgym.api.dto.request.ClienteRequestDTO;
import com.goldgym.api.dto.response.ClienteResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize; 
// --- IMPORTACIONES NECESARIAS PARA OBTENER EL USUARIO LOGUEADO ---
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // ----------------------------------------------------------------------
    // --- MÉTODO AGREGADO: Obtener Perfil del Usuario Autenticado ---
    // ----------------------------------------------------------------------
    @GetMapping("/profile/me")
    // NOTA: @PreAuthorize es redundante aquí porque ya lo manejamos en SecurityConfig,
    // pero es buena práctica de seguridad a nivel de método.
    //@PreAuthorize("hasAnyAuthority('CLIENTE')") 
    public ResponseEntity<ClienteResponseDTO> getMyProfile() {
        // 1. Obtener el nombre de usuario (username) del contexto de seguridad
        // Este nombre de usuario es la clave para buscar al cliente
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); 

        // 2. Llamar al servicio para buscar al cliente por su username
        // Necesitas un método en tu ClienteService que busque por username.
        ClienteResponseDTO clienteProfile = clienteService.obtenerClientePorUsername(username); 
        
        if (clienteProfile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clienteProfile);
    }
    // ----------------------------------------------------------------------

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.crear(cliente));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody ClienteRequestDTO clienteRequestDTO) {
        return ResponseEntity.ok(clienteService.actualizar(id, clienteRequestDTO));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") 
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
   // @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") 
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }
}