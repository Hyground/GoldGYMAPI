package com.goldgym.api.services;

import com.goldgym.api.entities.PlanMembresia;
import com.goldgym.api.repository.PlanMembresiaRepository;
import com.goldgym.api.dto.response.PlanAnaliticasDTO;
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
public class PlanMembresiaService {

    private final PlanMembresiaRepository planMembresiaRepository;

    public PlanMembresia crear(PlanMembresia plan) {
        plan.setCreadoEn(LocalDateTime.now());
        return planMembresiaRepository.save(plan);
    }

    public PlanMembresia actualizar(Long id, PlanMembresia actualizado) {
        PlanMembresia existente = planMembresiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan de membresía no encontrado"));
        existente.setNombre(actualizado.getNombre());
        existente.setDescripcion(actualizado.getDescripcion());
        existente.setPrecio(actualizado.getPrecio());
        existente.setDuracionDias(actualizado.getDuracionDias());
        existente.setReglasAcceso(actualizado.getReglasAcceso());
        existente.setActivo(actualizado.getActivo());
        existente.setActualizadoEn(LocalDateTime.now());
        return planMembresiaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!planMembresiaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan de membresía no encontrado");
        }
        planMembresiaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PlanMembresia> listar() {
        return planMembresiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PlanMembresia obtenerPorId(Long id) {
        return planMembresiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan de membresía no encontrado"));
    }
    
    // NUEVO MÉTODO DE ANALÍTICA
    @Transactional(readOnly = true)
    public List<PlanAnaliticasDTO> obtenerAnaliticas() {
        return planMembresiaRepository.findPlanAnaliticas();
    }
}
