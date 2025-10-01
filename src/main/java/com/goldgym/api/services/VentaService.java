package com.goldgym.api.services;

import com.goldgym.api.entities.Venta;
import com.goldgym.api.repository.VentaRepository;
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
public class VentaService {

    private final VentaRepository ventaRepository;

    public Venta crear(Venta venta) {
        venta.setFechaVenta(LocalDateTime.now());
        if (venta.getDetalles() != null) {
            venta.getDetalles().forEach(d -> d.setVenta(venta));
        }
        return ventaRepository.save(venta);
    }

    public Venta actualizar(Long id, Venta actualizado) {
        Venta existente = ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        existente.setFechaVenta(actualizado.getFechaVenta());
        existente.setCliente(actualizado.getCliente());
        existente.setUsuario(actualizado.getUsuario());
        existente.setTotal(actualizado.getTotal());
        existente.setNotas(actualizado.getNotas());
        existente.getDetalles().clear();
        if (actualizado.getDetalles() != null) {
            actualizado.getDetalles().forEach(d -> d.setVenta(existente));
            existente.getDetalles().addAll(actualizado.getDetalles());
        }
        return ventaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada");
        }
        ventaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
    }
}
