package com.goldgym.api.services;

import com.goldgym.api.entities.Producto;
import com.goldgym.api.repository.ProductoRepository;
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
public class ProductoService {

    private final ProductoRepository productoRepository;

    public Producto crear(Producto producto) {
        producto.setCreadoEn(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto actualizado) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        existente.setNombre(actualizado.getNombre());
        existente.setCategoria(actualizado.getCategoria());
        existente.setTipoMedida(actualizado.getTipoMedida());
        existente.setScoopsPorEnvase(actualizado.getScoopsPorEnvase());
        existente.setPrecioVenta(actualizado.getPrecioVenta());
        existente.setStockCantidad(actualizado.getStockCantidad());
        existente.setStockMinimoAlerta(actualizado.getStockMinimoAlerta());
        existente.setActivo(actualizado.getActivo());
        existente.setActualizadoEn(LocalDateTime.now());
        return productoRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }
}
