package com.goldgym.api.service;

import com.goldgym.api.domain.PlanMembresia;
import com.goldgym.api.repository.PlanMembresiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class PlanMembresiaService {
  private final PlanMembresiaRepository repo;

  public PlanMembresia crear(PlanMembresia p){
    if (repo.existsByNombreIgnoreCase(p.getNombre()))
      throw new IllegalArgumentException("Ya existe un plan con ese nombre");
    return repo.save(p);
  }
}
