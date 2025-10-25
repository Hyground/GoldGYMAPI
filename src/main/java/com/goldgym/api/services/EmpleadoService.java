package com.goldgym.api.services;

import com.goldgym.api.dto.response.EmpleadoResponseDTO;
import com.goldgym.api.entities.Empleado;
import com.goldgym.api.entities.Persona;
import com.goldgym.api.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ¡Importante!
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional // Aplica transacción a todos los métodos públicos por defecto
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    // Asumimos que también tienes PersonaRepository si necesitas actualizar Persona directamente
    // private final PersonaRepository personaRepository;

    public Empleado crear(Empleado empleado) {
        empleado.setCreadoEn(LocalDateTime.now());
        empleado.setActivo(true);
        // Generar código de empleado si es necesario
        // empleado.setCodigoEmpleado(...);
        return empleadoRepository.save(empleado);
    }

    public Empleado actualizar(Long id, Empleado actualizado) {
        Empleado existente = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));

        // Actualizar datos de Persona (simplificado, asume que vienen en 'actualizado.getPersona()')
        // Una mejor aproximación usaría un EmpleadoRequestDTO como en ClienteService
        if (actualizado.getPersona() != null && existente.getPersona() != null) {
            Persona personaExistente = existente.getPersona();
            Persona personaActualizada = actualizado.getPersona();
            personaExistente.setNombre(personaActualizada.getNombre());
            personaExistente.setApellido(personaActualizada.getApellido());
            personaExistente.setCorreo(personaActualizada.getCorreo());
            // ... copiar otros campos de persona
            personaExistente.setActualizadoEn(LocalDateTime.now());
        }

        // Actualizar datos de Empleado
        existente.setSalario(actualizado.getSalario());
        existente.setFechaContratacion(actualizado.getFechaContratacion());
        existente.setActivo(actualizado.getActivo()); // Permitir actualizar estado
        existente.setActualizadoEn(LocalDateTime.now());
        // existente.setCodigoEmpleado(actualizado.getCodigoEmpleado()); // Si aplica
        // existente.setEstado(actualizado.getEstado()); // Si aplica

        return empleadoRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
        }
        // Considerar desactivar en lugar de borrar
        empleadoRepository.deleteById(id);
    }

    // --- MÉTODOS MODIFICADOS/NUEVOS PARA USAR DTOs ---

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> listar() {
        return empleadoRepository.findAll().stream()
                .map(this::mapEmpleadoToDTO) // Mapear a DTO
                .collect(Collectors.toList());
    }

    // Método original que devuelve entidad (para uso interno si es necesario)
    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
    }

    // NUEVO: Método que devuelve DTO
    @Transactional(readOnly = true) // Necesario para acceder a datos LAZY de Persona
    public EmpleadoResponseDTO obtenerEmpleadoDTOPorId(Long id) {
        Empleado empleado = obtenerPorId(id); // Reutiliza el método anterior
        return mapEmpleadoToDTO(empleado);
    }


    // --- HELPER METHOD ---

    // Método helper privado para mapear Entidad Empleado a EmpleadoResponseDTO
    private EmpleadoResponseDTO mapEmpleadoToDTO(Empleado empleado) {
        if (empleado == null) return null;

        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setId(empleado.getId());
        // dto.setPuesto(empleado.getPuesto()); // Si tuvieras un campo puesto
        dto.setSalario(empleado.getSalario());
        dto.setFechaContratacion(empleado.getFechaContratacion());
        dto.setActivo(empleado.getActivo());

        // Acceder a Persona DENTRO de la transacción
        Persona persona = empleado.getPersona(); // Hibernate cargará el proxy
        if (persona != null) {
            dto.setNombrePersona((persona.getNombre() != null ? persona.getNombre() : "") + " " + (persona.getApellido() != null ? persona.getApellido() : ""));
            dto.setCorreoPersona(persona.getCorreo());
            // Podrías añadir más campos de Persona al DTO si los necesitas
        } else {
             dto.setNombrePersona("Persona no asignada");
             dto.setCorreoPersona("N/A");
        }

        return dto;
    }
}
