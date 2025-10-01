package com.goldgym.api.services;

import com.goldgym.api.entities.EventoLogin;
import com.goldgym.api.repository.EventoLoginRepository;
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
public class EventoLoginService {

    private final EventoLoginRepository eventoLoginRepository;

    public EventoLogin crear(EventoLogin eventoLogin) {
        eventoLogin.setCreadoEn(LocalDateTime.now());
        return eventoLoginRepository.save(eventoLogin);
    }

    public EventoLogin actualizar(Long id, EventoLogin actualizado) {
        EventoLogin existente = eventoLoginRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EventoLogin no encontrado"));
        existente.setUsuario(actualizado.getUsuario());
        existente.setExito(actualizado.getExito());
        existente.setIp(actualizado.getIp());
        existente.setUserAgent(actualizado.getUserAgent());
        existente.setDetalle(actualizado.getDetalle());
        return eventoLoginRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!eventoLoginRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EventoLogin no encontrado");
        }
        eventoLoginRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EventoLogin> listar() {
        return eventoLoginRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EventoLogin obtenerPorId(Long id) {
        return eventoLoginRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EventoLogin no encontrado"));
    }
}
