package com.goldgym.api.services;

import com.goldgym.api.dto.response.EmpleadoResponseDTO;
import com.goldgym.api.entities.Empleado;
import com.goldgym.api.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public Empleado crear(Empleado empleado) {
        empleado.setCreadoEn(LocalDateTime.now());
        return empleadoRepository.save(empleado);
    }

    public Empleado actualizar(Long id, Empleado actualizado) {
        Empleado existente = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
        existente.setPersona(actualizado.getPersona());
        existente.setCodigoEmpleado(actualizado.getCodigoEmpleado());
        existente.setFechaContratacion(actualizado.getFechaContratacion());
        existente.setSalario(actualizado.getSalario());
        existente.setEstado(actualizado.getEstado());
        existente.setActivo(actualizado.getActivo());
        existente.setActualizadoEn(LocalDateTime.now());
        return empleadoRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
        }
        empleadoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> listar() {
        return empleadoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private EmpleadoResponseDTO convertToDto(Empleado empleado) {
        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setId(empleado.getId());
        dto.setActivo(empleado.getActivo());
        dto.setSalario(empleado.getSalario());
        dto.setFechaContratacion(empleado.getFechaContratacion());
        // La entidad Empleado no tiene 'puesto', se puede añadir si es necesario.
        // dto.setPuesto(empleado.getPuesto()); 
        if (empleado.getPersona() != null) {
            dto.setNombrePersona(empleado.getPersona().getNombre() + " " + empleado.getPersona().getApellido());
            dto.setCorreoPersona(empleado.getPersona().getCorreo());
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
    }
}