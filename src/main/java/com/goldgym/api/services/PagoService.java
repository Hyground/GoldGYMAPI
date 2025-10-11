package com.goldgym.api.services;

import com.goldgym.api.dto.response.ClientePagoStatusDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.entities.Pago;
import com.goldgym.api.repository.ClienteRepository;
import com.goldgym.api.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ClienteRepository clienteRepository;

    public Pago crear(Pago pago) {
        pago.setCreadoEn(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    public Pago actualizar(Long id, Pago actualizado) {
        Pago existente = pagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
        existente.setMembresia(actualizado.getMembresia());
        existente.setCliente(actualizado.getCliente());
        existente.setFechaPago(actualizado.getFechaPago());
        existente.setMontoPagado(actualizado.getMontoPagado());
        existente.setFechaVencimiento(actualizado.getFechaVencimiento());
        existente.setEstado(actualizado.getEstado());
        existente.setActualizadoEn(LocalDateTime.now());
        return pagoRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado");
        }
        pagoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Pago> obtenerPagosPorCliente(Long clienteId) {
        return pagoRepository.findByClienteId(clienteId);
    }
    
    @Transactional(readOnly = true)
    public List<ClientePagoStatusDTO> getClientesConEstadoPago() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(cliente -> {
            ClientePagoStatusDTO dto = new ClientePagoStatusDTO();
            dto.setId(cliente.getId());
            dto.setNombreCompleto(cliente.getPersona().getNombre() + " " + cliente.getPersona().getApellido());
            dto.setCorreo(cliente.getPersona().getCorreo());
            dto.setCodigoCliente(cliente.getCodigo());

            // Buscar el último pago del cliente
            // Asumimos que el repositorio de pagos tiene un método para esto
            // Si no, necesitaríamos añadirlo: findTopByClienteOrderByFechaVencimientoDesc
            Pago ultimoPago = pagoRepository.findTopByClienteOrderByFechaVencimientoDesc(cliente)
                    .orElse(null);

            if (ultimoPago == null) {
                dto.setEstadoPago("ROJO"); // Sin pagos, se considera en deuda
                dto.setFechaVencimiento(null);
                dto.setMontoPendiente(0.0); // O un valor por defecto
            } else {
                dto.setFechaVencimiento(ultimoPago.getFechaVencimiento());
                
                // --- CORRECCIÓN DE NULLPOINTEREXCEPTION AQUÍ ---
                // Obtener el estado, o usar una cadena segura ("") si es null
                String estadoPago = ultimoPago.getEstado();
                if (estadoPago == null || estadoPago.isEmpty()) {
                    dto.setEstadoPago("ROJO"); // Asumir el peor caso si el estado no está definido
                    dto.setMontoPendiente(0.0);
                    return dto; // Salir del switch
                }
                switch (estadoPago) { // Usamos la variable local segura
                    case "PAGADO":
                        dto.setEstadoPago("VERDE");
                        dto.setMontoPendiente(0.0);
                        break;
                    case "PENDIENTE":
                        // Si está pendiente, se verifica el vencimiento
                        if (ultimoPago.getFechaVencimiento().isBefore(LocalDate.now())) {
                            dto.setEstadoPago("ROJO"); // Vencido
                            dto.setMontoPendiente(ultimoPago.getMontoPagado()); 
                        } else if (ultimoPago.getFechaVencimiento().isBefore(LocalDate.now().plusDays(7))) {
                            dto.setEstadoPago("AMARILLO"); // Próximo a vencer
                            dto.setMontoPendiente(ultimoPago.getMontoPagado());
                        } else {
                            dto.setEstadoPago("VERDE"); // Pendiente pero con fecha lejana
                            dto.setMontoPendiente(ultimoPago.getMontoPagado());
                        }
                        break;
                    case "VENCIDO":
                        dto.setEstadoPago("ROJO");
                        dto.setMontoPendiente(ultimoPago.getMontoPagado());
                        break;
                    default:
                        dto.setEstadoPago("ROJO"); // Estado desconocido en el switch
                        dto.setMontoPendiente(ultimoPago.getMontoPagado());
                        break;
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
