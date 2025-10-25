package com.goldgym.api.services;

import com.goldgym.api.dto.response.EmpleadoResponseDTO;
import com.goldgym.api.entities.Empleado;
import com.goldgym.api.entities.Persona; // Importar
import com.goldgym.api.repository.EmpleadoRepository;
import com.goldgym.api.repository.PersonaRepository; // Importar
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter; // Formateador
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate; // Importar

@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PersonaRepository personaRepository; // Inyectar repo Persona
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD


    public Empleado crear(Empleado empleado) {
        // Lógica para asegurar Persona, generar código, etc.
        // ...
        return empleadoRepository.save(empleado);
    }

     // Actualizar SÍ puede recibir Entidad si el frontend la envía completa (modal unificado)
     public Empleado actualizar(Long id, Empleado empleadoActualizado) {
          Empleado existente = empleadoRepository.findById(id)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));

          // Actualizar Persona asociada (si viene en el request y existe)
          if (empleadoActualizado.getPersona() != null && existente.getPersona() != null) {
               Persona personaActualizada = empleadoActualizado.getPersona();
               Persona personaExistente = existente.getPersona();

               // Mapear campos editables de Persona
               personaExistente.setNombre(personaActualizada.getNombre());
               personaExistente.setApellido(personaActualizada.getApellido());
               personaExistente.setCorreo(personaActualizada.getCorreo());
               personaExistente.setTelefono(personaActualizada.getTelefono());
               personaExistente.setFechaNacimiento(personaActualizada.getFechaNacimiento());
               personaExistente.setSexo(personaActualizada.getSexo());
               personaExistente.setEstadoCivil(personaActualizada.getEstadoCivil());
               personaExistente.setDireccion(personaActualizada.getDireccion());
               personaExistente.setTelefonoEmergencia(personaActualizada.getTelefonoEmergencia());
               personaExistente.setNotas(personaActualizada.getNotas());
               // activo de persona?
          }

          // Actualizar campos de Empleado
          existente.setSalario(empleadoActualizado.getSalario());
          existente.setFechaContratacion(empleadoActualizado.getFechaContratacion());
          existente.setActivo(empleadoActualizado.getActivo()); // Permitir activar/desactivar empleado
          // existente.setEstado(empleadoActualizado.getEstado()); // Si tuvieras estado

          return empleadoRepository.save(existente);
     }


    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
        }
        empleadoRepository.deleteById(id);
    }

    // Listar DTOs
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> listar() {
        return empleadoRepository.findAll().stream()
            .map(this::mapEmpleadoToDTO)
            .collect(Collectors.toList());
    }

     // Obtener Entidad (uso interno)
     @Transactional(readOnly = true)
     public Empleado obtenerPorId(Long id) {
         return empleadoRepository.findById(id)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
     }

    // Obtener DTO por ID
    @Transactional(readOnly = true)
    public EmpleadoResponseDTO obtenerEmpleadoDTOPorId(Long id) {
        Empleado empleado = obtenerPorId(id); // Reutiliza
        return mapEmpleadoToDTO(empleado);
    }

    // Helper para mapear Empleado a DTO
    public EmpleadoResponseDTO mapEmpleadoToDTO(Empleado empleado) {
        if (empleado == null) return null;
        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setId(empleado.getId());
        dto.setSalario(empleado.getSalario());
        dto.setFechaContratacion(empleado.getFechaContratacion()); // Dejar como LocalDate aquí
        dto.setActivo(empleado.getActivo());

        Persona persona = empleado.getPersona();
        if (persona != null) {
            dto.setPersonaId(persona.getId()); // <-- Incluir ID Persona
            dto.setNombre(persona.getNombre());
            dto.setApellido(persona.getApellido());
            dto.setCorreo(persona.getCorreo());
            dto.setTelefono(persona.getTelefono());
            dto.setFechaNacimiento(persona.getFechaNacimiento() != null ? persona.getFechaNacimiento().format(DATE_FORMATTER) : null); // String YYYY-MM-DD
            dto.setSexo(persona.getSexo());
            dto.setEstadoCivil(persona.getEstadoCivil());
            dto.setDireccion(persona.getDireccion());
            dto.setTelefonoEmergencia(persona.getTelefonoEmergencia());
            dto.setNotas(persona.getNotas());

            // Campos combinados
            dto.setNombrePersona(((persona.getNombre() != null ? persona.getNombre() : "") + " " + (persona.getApellido() != null ? persona.getApellido() : "")).trim());
            dto.setEmailPersona(persona.getCorreo());
        } else {
             dto.setNombrePersona("Persona no asociada");
             dto.setEmailPersona("N/A");
        }
        return dto;
    }
}
