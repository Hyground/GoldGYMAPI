package com.goldgym.api.controllers;

import com.goldgym.api.dto.request.ClienteRequestDTO;
// --- CAMBIO: Importar ClienteResponseDTO ---
import com.goldgym.api.dto.response.ClienteResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/profile/me")
    public ResponseEntity<ClienteResponseDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ClienteResponseDTO clienteProfile = clienteService.obtenerClientePorUsername(username);
        if (clienteProfile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clienteProfile);
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        // Nota: Idealmente, crear también debería usar un DTO de Request y devolver DTO de Response
        return ResponseEntity.ok(clienteService.crear(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody ClienteRequestDTO clienteRequestDTO) {
         // Nota: Idealmente, actualizar debería devolver ClienteResponseDTO
        return ResponseEntity.ok(clienteService.actualizar(id, clienteRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        // Asumiendo que clienteService.listar() ya devuelve List<ClienteResponseDTO>
        return ResponseEntity.ok(clienteService.listar());
    }

    // --- MÉTODO MODIFICADO ---
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        // Llamar al método del servicio que ahora devuelve ClienteResponseDTO
        ClienteResponseDTO clienteDTO = clienteService.obtenerClienteDTOPorId(id); // Necesitarás crear este método en el Service
        return ResponseEntity.ok(clienteDTO);
    }

     // --- NUEVO MÉTODO AGREGADO PARA BÚSQUEDA EN MODAL DE PAGO ---
     @GetMapping("/buscar")
     public ResponseEntity<List<ClienteResponseDTO>> buscarClientes(@RequestParam("query") String query) {
          // Asumiendo que tienes un método en ClienteService para buscar
          return ResponseEntity.ok(clienteService.buscarClientesPorQuery(query));
     }
}
