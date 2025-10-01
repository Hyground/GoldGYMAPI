package com.goldgym.api.services;

import com.goldgym.api.entities.DetalleVenta;
import com.goldgym.api.repository.DetalleVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVenta crear(DetalleVenta detalle) {
        return detalleVentaRepository.save(detalle);
    }

    public DetalleVenta actualizar(Long id, DetalleVenta actualizado) {
        DetalleVenta existente = detalleVentaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de venta no encontrado"));
        existente.setVenta(actualizado.getVenta());
        existente.setProducto(actualizado.getProducto());
        existente.setCantidad(actualizado.getCantidad());
        existente.setPrecioUnitario(actualizado.getPrecioUnitario());
        existente.setSubtotal(actualizado.getSubtotal());
        return detalleVentaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de venta no encontrado");
        }
        detalleVentaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DetalleVenta> listar() {
        return detalleVentaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DetalleVenta obtenerPorId(Long id) {
        return detalleVentaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de venta no encontrado"));
    }
}
