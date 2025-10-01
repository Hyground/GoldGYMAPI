package com.goldgym.api.services;

import com.goldgym.api.entities.UsuarioRol;
import com.goldgym.api.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioRolService {

    private final UsuarioRolRepository usuarioRolRepository;

    public UsuarioRol crear(UsuarioRol usuarioRol) {
        return usuarioRolRepository.save(usuarioRol);
    }

    public UsuarioRol actualizar(Long id, UsuarioRol actualizado) {
        UsuarioRol existente = usuarioRolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UsuarioRol no encontrado"));
        existente.setUsuario(actualizado.getUsuario());
        existente.setRol(actualizado.getRol());
        return usuarioRolRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!usuarioRolRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UsuarioRol no encontrado");
        }
        usuarioRolRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UsuarioRol> listar() {
        return usuarioRolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UsuarioRol obtenerPorId(Long id) {
        return usuarioRolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UsuarioRol no encontrado"));
    }
}
