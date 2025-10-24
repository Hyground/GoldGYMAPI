package com.goldgym.api.services;

import com.goldgym.api.dto.request.VentaRequestDTO;
import com.goldgym.api.dto.response.VentaResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.entities.DetalleVenta;
import com.goldgym.api.entities.Producto;
import com.goldgym.api.entities.Venta;
import com.goldgym.api.repository.ClienteRepository;
import com.goldgym.api.repository.DetalleVentaRepository;
import com.goldgym.api.repository.ProductoRepository;
import com.goldgym.api.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public Venta crear(VentaRequestDTO ventaRequest) {
        Cliente cliente = null;
        if (ventaRequest.getClienteId() != null) {
            cliente = clienteRepository.findById(ventaRequest.getClienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        }

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTotal(ventaRequest.getTotal());
        Venta savedVenta = ventaRepository.save(venta);

        List<DetalleVenta> detalles = new ArrayList<>();
        for (Long productoId : ventaRequest.getProductosIds()) {
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(savedVenta);
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setCantidad(1.0); // Corregido a Double
            detalles.add(detalleVentaRepository.save(detalle));
        }
        savedVenta.setDetalles(detalles);
        return savedVenta;
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
    public List<VentaResponseDTO> listarVentasDTO() {
        return ventaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private VentaResponseDTO convertToDto(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(venta.getId());
        dto.setTotal(venta.getTotal());
        dto.setFecha(venta.getFechaVenta());

        if (venta.getCliente() != null) {
            dto.setClienteId(venta.getCliente().getId());
            dto.setClienteNombre(venta.getCliente().getPersona().getNombre() + " " + venta.getCliente().getPersona().getApellido());
        } else {
            dto.setClienteNombre("Cliente General");
        }

        if (venta.getDetalles() != null) {
            dto.setProductos(venta.getDetalles().stream()
                    .map(detalle -> detalle.getProducto().getNombre())
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
    }
}
