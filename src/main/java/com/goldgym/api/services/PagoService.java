package com.goldgym.api.services;

import com.goldgym.api.entities.Pago;
import com.goldgym.api.repository.PagoRepository;
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
public class PagoService {

    private final PagoRepository pagoRepository;

    public Pago crear(Pago pago) {
        pago.setCreadoEn(LocalDateTime.now());
        pago.setPagadoEn(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    public Pago actualizar(Long id, Pago actualizado) {
        Pago existente = pagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
        existente.setCliente(actualizado.getCliente());
        existente.setMembresia(actualizado.getMembresia());
        existente.setMonto(actualizado.getMonto());
        existente.setMoneda(actualizado.getMoneda());
        existente.setMetodoPago(actualizado.getMetodoPago());
        existente.setPagadoEn(actualizado.getPagadoEn());
        existente.setReferencia(actualizado.getReferencia());
        existente.setNotas(actualizado.getNotas());
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
}
