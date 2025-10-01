package com.goldgym.api.services;

import com.goldgym.api.entities.Medicion;
import com.goldgym.api.repository.MedicionRepository;
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
public class MedicionService {

    private final MedicionRepository medicionRepository;

    public Medicion crear(Medicion medicion) {
        medicion.setCreadoEn(LocalDateTime.now());
        return medicionRepository.save(medicion);
    }

    public Medicion actualizar(Long id, Medicion actualizado) {
        Medicion existente = medicionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medición no encontrada"));
        existente.setCliente(actualizado.getCliente());
        existente.setFechaMedicion(actualizado.getFechaMedicion());
        existente.setPesoKg(actualizado.getPesoKg());
        existente.setAlturaCm(actualizado.getAlturaCm());
        existente.setBrazoCm(actualizado.getBrazoCm());
        existente.setPiernaCm(actualizado.getPiernaCm());
        existente.setCinturaCm(actualizado.getCinturaCm());
        existente.setCuelloCm(actualizado.getCuelloCm());
        existente.setNotas(actualizado.getNotas());
        existente.setActualizadoEn(LocalDateTime.now());
        return medicionRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!medicionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medición no encontrada");
        }
        medicionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Medicion> listar() {
        return medicionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Medicion obtenerPorId(Long id) {
        return medicionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medición no encontrada"));
    }
}
