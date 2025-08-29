package com.goldgym.api.controller;

import com.goldgym.api.domain.PlanMembresia;
import com.goldgym.api.repository.PlanMembresiaRepository;
import com.goldgym.api.service.PlanMembresiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController @RequestMapping("/api/planes") @RequiredArgsConstructor
public class PlanMembresiaController {
  private final PlanMembresiaService service;
  private final PlanMembresiaRepository repo;

  @PostMapping
  public ResponseEntity<PlanMembresia> crear(@RequestBody PlanMembresia plan){
    return ResponseEntity.ok(service.crear(plan));
  }

  @GetMapping public ResponseEntity<?> listar(){ return ResponseEntity.ok(repo.findAll()); }

  @GetMapping("/{id}") public ResponseEntity<?> porId(@PathVariable UUID id){
    return ResponseEntity.of(repo.findById(id));
  }
}
