package com.goldgym.api.controllers;

import com.goldgym.api.entities.ParametroSistema;
import com.goldgym.api.services.ParametroSistemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parametros")
@RequiredArgsConstructor
public class ParametroSistemaController {

    private final ParametroSistemaService parametroService;

    @PostMapping
    public ResponseEntity<ParametroSistema> crear(@RequestBody ParametroSistema parametro) {
        return ResponseEntity.ok(parametroService.crear(parametro));
    }

    @PutMapping("/{clave}")
    public ResponseEntity<ParametroSistema> actualizar(@PathVariable String clave, @RequestBody ParametroSistema parametro) {
        return ResponseEntity.ok(parametroService.actualizar(clave, parametro));
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        parametroService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ParametroSistema>> listar() {
        return ResponseEntity.ok(parametroService.listar());
    }

    @GetMapping("/{clave}")
    public ResponseEntity<ParametroSistema> obtenerPorClave(@PathVariable String clave) {
        return ResponseEntity.ok(parametroService.obtenerPorClave(clave));
    }
}
