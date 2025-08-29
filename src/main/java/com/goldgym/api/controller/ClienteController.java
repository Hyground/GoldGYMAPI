package com.goldgym.api.controller;

import java.util.List;
import com.goldgym.api.domain.Cliente;
import com.goldgym.api.domain.Persona;
import com.goldgym.api.dto.cliente.CrearClienteRequest;
import com.goldgym.api.repository.ClienteRepository;
import com.goldgym.api.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController @RequestMapping("/api/clientes") @RequiredArgsConstructor
public class ClienteController {
  private final PersonaRepository personaRepo;
  private final ClienteRepository clienteRepo;
  private final ClienteRepository repo;

  @PostMapping
  public ResponseEntity<Cliente> crear(@RequestBody CrearClienteRequest r){
    var p = Persona.builder()
        .nombres(r.getNombres()).apellidos(r.getApellidos())
        .fechaNacimiento(r.getFechaNacimiento()).sexo(r.getSexo())
        .telefono(r.getTelefono()).correo(r.getCorreo())
        .direccion(r.getDireccion()).activo(true).build();
    personaRepo.save(p);
    var c = Cliente.builder().persona(p).codigo(r.getCodigo()).estado("activo").build();
    return ResponseEntity.ok(clienteRepo.save(c));
  }

  @GetMapping("/{id}") public ResponseEntity<Cliente> porId(@PathVariable UUID id){
    return ResponseEntity.of(clienteRepo.findById(id));
  }

  @GetMapping
  public List<Cliente> listar() { return repo.findAll(); }
}