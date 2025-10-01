package com.goldgym.api.services;

import com.goldgym.api.entities.Asistencia;
import com.goldgym.api.repository.AsistenciaRepository;
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
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    public Asistencia crear(Asistencia asistencia) {
        asistencia.setCreadoEn(LocalDateTime.now());
        return asistenciaRepository.save(asistencia);
    }

    public Asistencia actualizar(Long id, Asistencia actualizado) {
        Asistencia existente = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asistencia no encontrada"));
        existente.setCliente(actualizado.getCliente());
        existente.setFecha(actualizado.getFecha());
        existente.setPresente(actualizado.getPresente());
        return asistenciaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!asistenciaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asistencia no encontrada");
        }
        asistenciaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Asistencia> listar() {
        return asistenciaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Asistencia obtenerPorId(Long id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asistencia no encontrada"));
    }
}
