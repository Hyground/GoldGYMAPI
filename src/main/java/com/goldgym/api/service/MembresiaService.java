package com.goldgym.api.service;

import com.goldgym.api.domain.*;
import com.goldgym.api.dto.membresia.CrearMembresiaRequest;
import com.goldgym.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class MembresiaService {
  private final MembresiaRepository repo;
  private final ClienteRepository clienteRepo;
  private final PlanMembresiaRepository planRepo;

  public Membresia crear(CrearMembresiaRequest r){
    var cliente = clienteRepo.findById(r.getClienteId())
        .orElseThrow(() -> new IllegalArgumentException("Cliente no existe"));
    var plan = planRepo.findById(r.getPlanId())
        .orElseThrow(() -> new IllegalArgumentException("Plan no existe"));

    var m = Membresia.builder()
        .cliente(cliente).plan(plan)
        .fechaInicio(r.getFechaInicio()).fechaFin(r.getFechaFin())
        .precioContratado(r.getPrecioContratado())
        .estado("activa").build();
    return repo.save(m);
  }
}
