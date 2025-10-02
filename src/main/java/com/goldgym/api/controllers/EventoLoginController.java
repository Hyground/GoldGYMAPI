package com.goldgym.api.controllers;

import com.goldgym.api.entities.EventoLogin;
import com.goldgym.api.services.EventoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos-login")
@RequiredArgsConstructor
public class EventoLoginController {

    private final EventoLoginService eventoLoginService;

    @PostMapping
    public ResponseEntity<EventoLogin> crear(@RequestBody EventoLogin eventoLogin) {
        return ResponseEntity.ok(eventoLoginService.crear(eventoLogin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eventoLoginService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EventoLogin>> listar() {
        return ResponseEntity.ok(eventoLoginService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoLogin> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(eventoLoginService.obtenerPorId(id));
    }
}
