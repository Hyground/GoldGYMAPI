package com.goldgym.api.service;

import com.goldgym.api.domain.Cliente;
import com.goldgym.api.domain.Persona;
import com.goldgym.api.dto.cliente.CrearClienteRequest;
import com.goldgym.api.repository.ClienteRepository;
import com.goldgym.api.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class ClienteService {
  private final ClienteRepository clienteRepo;
  private final PersonaRepository personaRepo;

  public Cliente crear(CrearClienteRequest r){
    var p = Persona.builder()
        .nombres(r.getNombres()).apellidos(r.getApellidos())
        .fechaNacimiento(r.getFechaNacimiento()).sexo(r.getSexo())
        .telefono(r.getTelefono()).correo(r.getCorreo())
        .direccion(r.getDireccion()).activo(true).build();
    personaRepo.save(p);
    var c = Cliente.builder().persona(p).codigo(r.getCodigo()).estado("activo").build();
    return clienteRepo.save(c);
  }
}
