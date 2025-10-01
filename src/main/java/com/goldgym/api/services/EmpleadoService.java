package com.goldgym.api.services;

import com.goldgym.api.entities.Empleado;
import com.goldgym.api.repository.EmpleadoRepository;
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
    public List<Empleado> listar() {
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado"));
    }
}
