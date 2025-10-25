package com.goldgym.api.services;

import com.goldgym.api.dto.response.UsuarioResponseDTO; // Importar DTO
import com.goldgym.api.entities.Persona; // Importar Persona
import com.goldgym.api.entities.Usuario;
import com.goldgym.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonaService personaService; // Inyectar PersonaService (si es necesario actualizar Persona)

    @Override
    @Transactional(readOnly = true) // Importante para acceder a roles y persona
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Acceder a getRoles() dentro de la transacción para cargarlos
        Set<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toSet());

        // Asegurarse de cargar la persona si es necesario (aunque UserDetails no la usa directamente)
        // Hibernate.initialize(usuario.getPersona()); // Opcional si se necesita fuera

        return new User(usuario.getUsername(), usuario.getPasswordHash(), authorities);
    }


    public Usuario crear(Usuario usuario) {
        // Asegurarse de que la persona exista o se cree si es necesario
        // (La lógica de creación unificada maneja esto mejor)
        if (usuario.getPersona() != null && usuario.getPersona().getId() == null) {
             // Si se pasa un objeto persona sin ID, intentar guardarlo primero
             // Esto puede ser complejo, el endpoint unificado es preferible
             Persona personaGuardada = personaService.crear(usuario.getPersona()); // Asume que tienes PersonaService
             usuario.setPersona(personaGuardada);
        } else if (usuario.getPersona() == null || usuario.getPersona().getId() == null) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere una Persona válida para crear un Usuario.");
        }


        usuario.setCreadoEn(LocalDateTime.now());
        // Codificar contraseña SOLO si no viene ya codificada (cuidado al llamar este método)
        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().startsWith("$2a$")) {
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        usuario.setActivo(true); // Activo por defecto al crear?
        usuario.setBloqueado(false);
        usuario.setIntentosFallidos(0);

        return usuarioRepository.save(usuario);
    }

    // Método Actualizar modificado para manejar datos de Persona y contraseña opcional
    public Usuario actualizar(Long id, Usuario actualizado) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Actualizar datos de Persona si vienen en el DTO/request anidado
        if (actualizado.getPersona() != null) {
             Persona personaActualizada = actualizado.getPersona();
             Persona personaExistente = existente.getPersona();
             if (personaExistente != null) {
                  // Actualizar campos de personaExistente con los de personaActualizada
                  personaExistente.setNombre(personaActualizada.getNombre());
                  personaExistente.setApellido(personaActualizada.getApellido());
                  personaExistente.setCorreo(personaActualizada.getCorreo());
                  personaExistente.setTelefono(personaActualizada.getTelefono());
                  personaExistente.setFechaNacimiento(personaActualizada.getFechaNacimiento());
                  personaExistente.setSexo(personaActualizada.getSexo());
                  personaExistente.setEstadoCivil(personaActualizada.getEstadoCivil());
                  personaExistente.setDireccion(personaActualizada.getDireccion());
                  personaExistente.setTelefonoEmergencia(personaActualizada.getTelefonoEmergencia());
                  personaExistente.setNotas(personaActualizada.getNotas());
                  personaExistente.setActualizadoEn(LocalDateTime.now());
                  // No guardar aquí, se guarda junto con el usuario
             } else {
                  // Esto no debería pasar si la relación es obligatoria, pero por si acaso
                  existente.setPersona(personaActualizada); // Asociar nueva persona? Raro en actualización.
             }
        }


        // Actualizar datos de Usuario
        existente.setUsername(actualizado.getUsername());
        // Actualizar contraseña SOLO si se proporciona una nueva (no vacía)
        if (actualizado.getPasswordHash() != null && !actualizado.getPasswordHash().isEmpty()) {
             // Verificar si ya está codificada (improbable que venga así del frontend)
             if (!actualizado.getPasswordHash().startsWith("$2a$")) {
                  existente.setPasswordHash(passwordEncoder.encode(actualizado.getPasswordHash()));
             } else {
                  existente.setPasswordHash(actualizado.getPasswordHash()); // Ya venía codificada? Raro.
             }
        } // Si no viene password, no se toca la existente

        // existente.setUltimoLogin(actualizado.getUltimoLogin()); // No actualizar desde aquí
        existente.setBloqueado(actualizado.getBloqueado());
        // existente.setIntentosFallidos(actualizado.getIntentosFallidos()); // No actualizar desde aquí
        existente.setActivo(actualizado.getActivo());
        // existente.setRoles(actualizado.getRoles()); // Actualizar roles requiere lógica separada, no hacerlo aquí directamente
        existente.setActualizadoEn(LocalDateTime.now());
        return usuarioRepository.save(existente);
    }


    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        // Considerar lógica adicional: ¿Desactivar en lugar de borrar? ¿Borrar relaciones?
        usuarioRepository.deleteById(id);
    }

    // Método Listar modificado para devolver DTO
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::mapUsuarioToDTO)
                .collect(Collectors.toList());
    }

    // Método Obtener por ID modificado para devolver DTO
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioDTOPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return mapUsuarioToDTO(usuario);
    }

     // Método para buscar Usuario por ID de Persona (usado en frontend)
     @Transactional(readOnly = true)
     public UsuarioResponseDTO obtenerUsuarioDTOPorPersonaId(Long personaId) {
          Usuario usuario = usuarioRepository.findByPersonaId(personaId) // Necesitas añadir findByPersonaId a UsuarioRepository
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado para la persona dada"));
          return mapUsuarioToDTO(usuario);
     }


    // --- Helper para mapear Entidad a DTO ---
    private UsuarioResponseDTO mapUsuarioToDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setActivo(usuario.getActivo());
        dto.setBloqueado(usuario.getBloqueado());

        // Mapear Persona (acceder dentro de la transacción)
        if (usuario.getPersona() != null) {
            Persona p = usuario.getPersona();
            dto.setPersonaId(p.getId());
            dto.setNombrePersona((p.getNombre() != null ? p.getNombre() : "") + " " + (p.getApellido() != null ? p.getApellido() : ""));
            dto.setEmailPersona(p.getCorreo());

            // Campos detallados
            dto.setNombre(p.getNombre());
            dto.setApellido(p.getApellido());
            dto.setCorreo(p.getCorreo());
            dto.setTelefono(p.getTelefono());
            dto.setFechaNacimiento(p.getFechaNacimiento() != null ? p.getFechaNacimiento().toString() : null);
            dto.setSexo(p.getSexo());
            dto.setEstadoCivil(p.getEstadoCivil());
            dto.setDireccion(p.getDireccion());
            dto.setTelefonoEmergencia(p.getTelefonoEmergencia());
            dto.setNotas(p.getNotas());
        }

        // Mapear Roles (acceder dentro de la transacción)
        if (usuario.getRoles() != null) {
            dto.setRoles(usuario.getRoles().stream()
                    .map(rol -> rol.getNombre())
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
