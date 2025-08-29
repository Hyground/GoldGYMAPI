package com.goldgym.api.controller;

import com.goldgym.api.domain.Membresia;
import com.goldgym.api.dto.membresia.CrearMembresiaRequest;
import com.goldgym.api.repository.MembresiaRepository;
import com.goldgym.api.service.MembresiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController @RequestMapping("/api/membresias") @RequiredArgsConstructor
public class MembresiaController {
  private final MembresiaService service;
  private final MembresiaRepository repo;

  @PostMapping
  public ResponseEntity<Membresia> crear(@RequestBody CrearMembresiaRequest req){
    return ResponseEntity.ok(service.crear(req));
  }

  @GetMapping public ResponseEntity<?> listar(){ return ResponseEntity.ok(repo.findAll()); }

  @GetMapping("/{id}") public ResponseEntity<?> porId(@PathVariable UUID id){
    return ResponseEntity.of(repo.findById(id));
  }
}
