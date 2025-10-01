package com.goldgym.api.services;

import com.goldgym.api.entities.Persona;
import com.goldgym.api.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonaService {

    private final PersonaRepository personaRepository;

    public Persona crear(Persona persona) {
        persona.setCreadoEn(LocalDateTime.now());
        return personaRepository.save(persona);
    }

    public Persona actualizar(Long id, Persona actualizado) {
        Persona existente = personaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
        existente.setNombre(actualizado.getNombre());
        existente.setApellido(actualizado.getApellido());
        existente.setFechaNacimiento(actualizado.getFechaNacimiento());
        existente.setSexo(actualizado.getSexo());
        existente.setEstadoCivil(actualizado.getEstadoCivil());
        existente.setTelefono(actualizado.getTelefono());
        existente.setCorreo(actualizado.getCorreo());
        existente.setDireccion(actualizado.getDireccion());
        existente.setTelefonoEmergencia(actualizado.getTelefonoEmergencia());
        existente.setNotas(actualizado.getNotas());
        existente.setActivo(actualizado.getActivo());
        existente.setActualizadoEn(LocalDateTime.now());
        return personaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!personaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        personaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Persona> listar() {
        return personaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Persona obtenerPorId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
    }
}
