package com.goldgym.api.services;

import com.goldgym.api.entities.Membresia;
import com.goldgym.api.repository.MembresiaRepository;
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
public class MembresiaService {

    private final MembresiaRepository membresiaRepository;

    public Membresia crear(Membresia membresia) {
        membresia.setCreadoEn(LocalDateTime.now());
        return membresiaRepository.save(membresia);
    }

    public Membresia actualizar(Long id, Membresia actualizado) {
        Membresia existente = membresiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membresía no encontrada"));
        existente.setCliente(actualizado.getCliente());
        existente.setPlan(actualizado.getPlan());
        existente.setFechaInicio(actualizado.getFechaInicio());
        existente.setFechaFin(actualizado.getFechaFin());
        existente.setPrecioContratado(actualizado.getPrecioContratado());
        existente.setEstado(actualizado.getEstado());
        existente.setNota(actualizado.getNota());
        existente.setActualizadoEn(LocalDateTime.now());
        return membresiaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!membresiaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Membresía no encontrada");
        }
        membresiaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Membresia> listar() {
        return membresiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Membresia obtenerPorId(Long id) {
        return membresiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membresía no encontrada"));
    }
}
