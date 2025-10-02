package com.goldgym.api.controllers;

import com.goldgym.api.entities.TokenRecuperacion;
import com.goldgym.api.services.TokenRecuperacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenRecuperacionController {

    private final TokenRecuperacionService tokenService;

    @PostMapping
    public ResponseEntity<TokenRecuperacion> crear(@RequestBody TokenRecuperacion token) {
        return ResponseEntity.ok(tokenService.crear(token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TokenRecuperacion> actualizar(@PathVariable Long id, @RequestBody TokenRecuperacion token) {
        return ResponseEntity.ok(tokenService.actualizar(id, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tokenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TokenRecuperacion>> listar() {
        return ResponseEntity.ok(tokenService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenRecuperacion> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tokenService.obtenerPorId(id));
    }
}
