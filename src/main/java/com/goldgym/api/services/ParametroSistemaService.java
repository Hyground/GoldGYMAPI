package com.goldgym.api.services;

import com.goldgym.api.entities.ParametroSistema;
import com.goldgym.api.repository.ParametroSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParametroSistemaService {

    private final ParametroSistemaRepository parametroSistemaRepository;

    public ParametroSistema crear(ParametroSistema parametro) {
        return parametroSistemaRepository.save(parametro);
    }

    public ParametroSistema actualizar(String clave, ParametroSistema actualizado) {
        ParametroSistema existente = parametroSistemaRepository.findById(clave)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parámetro no encontrado"));
        existente.setValor(actualizado.getValor());
        existente.setDescripcion(actualizado.getDescripcion());
        return parametroSistemaRepository.save(existente);
    }

    public void eliminar(String clave) {
        if (!parametroSistemaRepository.existsById(clave)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parámetro no encontrado");
        }
        parametroSistemaRepository.deleteById(clave);
    }

    @Transactional(readOnly = true)
    public List<ParametroSistema> listar() {
        return parametroSistemaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ParametroSistema obtenerPorClave(String clave) {
        return parametroSistemaRepository.findById(clave)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parámetro no encontrado"));
    }
}
