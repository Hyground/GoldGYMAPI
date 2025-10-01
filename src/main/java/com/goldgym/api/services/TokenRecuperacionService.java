package com.goldgym.api.services;

import com.goldgym.api.entities.TokenRecuperacion;
import com.goldgym.api.repository.TokenRecuperacionRepository;
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
public class TokenRecuperacionService {

    private final TokenRecuperacionRepository tokenRecuperacionRepository;

    public TokenRecuperacion crear(TokenRecuperacion token) {
        token.setCreadoEn(LocalDateTime.now());
        return tokenRecuperacionRepository.save(token);
    }

    public TokenRecuperacion actualizar(Long id, TokenRecuperacion actualizado) {
        TokenRecuperacion existente = tokenRecuperacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token de recuperación no encontrado"));
        existente.setUsuario(actualizado.getUsuario());
        existente.setCanal(actualizado.getCanal());
        existente.setDestino(actualizado.getDestino());
        existente.setCodigo(actualizado.getCodigo());
        existente.setToken(actualizado.getToken());
        existente.setExpiraEn(actualizado.getExpiraEn());
        existente.setUsadoEn(actualizado.getUsadoEn());
        existente.setIntento(actualizado.getIntento());
        return tokenRecuperacionRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!tokenRecuperacionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token de recuperación no encontrado");
        }
        tokenRecuperacionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TokenRecuperacion> listar() {
        return tokenRecuperacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TokenRecuperacion obtenerPorId(Long id) {
        return tokenRecuperacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token de recuperación no encontrado"));
    }
}
