package com.goldgym.api.services;

import com.goldgym.api.entities.CuestionarioSalud;
import com.goldgym.api.repository.CuestionarioSaludRepository;
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
public class CuestionarioSaludService {

    private final CuestionarioSaludRepository cuestionarioSaludRepository;

    public CuestionarioSalud crear(CuestionarioSalud cuestionario) {
        cuestionario.setCreadoEn(LocalDateTime.now());
        return cuestionarioSaludRepository.save(cuestionario);
    }

    public CuestionarioSalud actualizar(Long id, CuestionarioSalud actualizado) {
        CuestionarioSalud existente = cuestionarioSaludRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuestionario de salud no encontrado"));

        existente.setCliente(actualizado.getCliente());
        existente.setFechaRegistro(actualizado.getFechaRegistro());
        existente.setLesionOsea(actualizado.getLesionOsea());
        existente.setDescripcionLesionOsea(actualizado.getDescripcionLesionOsea());
        existente.setLesionMuscular(actualizado.getLesionMuscular());
        existente.setDescripcionLesionMuscular(actualizado.getDescripcionLesionMuscular());
        existente.setEnfermedadCardiovascular(actualizado.getEnfermedadCardiovascular());
        existente.setSeAsfixia(actualizado.getSeAsfixia());
        existente.setEmbarazada(actualizado.getEmbarazada());
        existente.setAnemia(actualizado.getAnemia());
        existente.setPracticaDeportes(actualizado.getPracticaDeportes());
        existente.setAsma(actualizado.getAsma());
        existente.setEpilepsia(actualizado.getEpilepsia());
        existente.setDiabetes(actualizado.getDiabetes());
        existente.setMareos(actualizado.getMareos());
        existente.setDesmayos(actualizado.getDesmayos());
        existente.setNausea(actualizado.getNausea());
        existente.setDificultadRespirar(actualizado.getDificultadRespirar());
        existente.setPrevioGimnasio(actualizado.getPrevioGimnasio());
        existente.setObjetivo(actualizado.getObjetivo());
        existente.setExtra(actualizado.getExtra());
        existente.setActualizadoEn(LocalDateTime.now());

        return cuestionarioSaludRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!cuestionarioSaludRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuestionario de salud no encontrado");
        }
        cuestionarioSaludRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CuestionarioSalud> listar() {
        return cuestionarioSaludRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CuestionarioSalud obtenerPorId(Long id) {
        return cuestionarioSaludRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuestionario de salud no encontrado"));
    }
}
