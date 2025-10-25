// Archivo: ProductoController.java

package com.goldgym.api.controllers;

import com.goldgym.api.entities.Producto;
import com.goldgym.api.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// IMPORTAR ESTO
//import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") // REQUERIDO PARA CREAR
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.crear(producto));
    }

    @PutMapping("/{id}")
   // @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')") // REQUERIDO PARA ACTUALIZAR
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR')") // REQUERIDO PARA ELIMINAR (ESTE ES EL QUE FALLA)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    // PERMITIR A TODOS VER EL INVENTARIO, incl. CLiente, si es necesario.
    // Si solo Admin/Empleado deben ver la lista, usa hasAnyAuthority('ADMINISTRADOR', 'EMPLEADO')
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    //@PreAuthorize("isAuthenticated()") // Permite ver el detalle a cualquier autenticado
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }
}