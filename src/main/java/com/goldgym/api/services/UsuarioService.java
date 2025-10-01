package com.goldgym.api.services;

import com.goldgym.api.entities.Usuario;
import com.goldgym.api.repository.UsuarioRepository;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario crear(Usuario usuario) {
        usuario.setCreadoEn(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario actualizado) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        existente.setPersona(actualizado.getPersona());
        existente.setUsername(actualizado.getUsername());
        existente.setPasswordHash(actualizado.getPasswordHash());
        existente.setUltimoLogin(actualizado.getUltimoLogin());
        existente.setBloqueado(actualizado.getBloqueado());
        existente.setIntentosFallidos(actualizado.getIntentosFallidos());
        existente.setActivo(actualizado.getActivo());
        existente.setRoles(actualizado.getRoles());
        existente.setActualizadoEn(LocalDateTime.now());
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
