package com.goldgym.api.services;

import com.goldgym.api.dto.response.UsuarioResponseDTO; // Importar DTO
import com.goldgym.api.entities.Persona; // Importar Persona
import com.goldgym.api.entities.Rol; // Importar Rol
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
import java.time.format.DateTimeFormatter; // Para formatear fechas
import java.util.Collections; // Para roles vacíos
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream; // Importar Stream

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD

    @Override
    @Transactional(readOnly = true) // Importante para cargar roles LAZY
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // *** CORRECCIÓN LOGIN USUARIO INACTIVO ***
        // Lanzar excepción si el usuario no está activo
        if (usuario.getActivo() == null || !usuario.getActivo()) {
             // Esta excepción es manejada por Spring Security y devuelve 403 Forbidden
             // Puedes cambiarla a DisabledException si prefieres ese manejo
             // throw new DisabledException("La cuenta de usuario está inactiva.");
             throw new UsernameNotFoundException("La cuenta de usuario está inactiva: " + username); // O usar esta
        }

        Set<GrantedAuthority> authorities = Collections.emptySet(); // Default a vacío
        if (usuario.getRoles() != null) { // Verificar nulidad
             authorities = usuario.getRoles().stream()
                 .map(rol -> new SimpleGrantedAuthority(rol.getNombre())) // Asume que Rol tiene getNombre()
                 .collect(Collectors.toSet());
        } else {
             System.err.println("Advertencia: Usuario " + username + " no tiene roles asignados.");
        }


        return new User(usuario.getUsername(), usuario.getPasswordHash(), authorities);
    }


    public Usuario crear(Usuario usuario) {
        usuario.setCreadoEn(LocalDateTime.now());
        // Codificar contraseña SÓLO si se proporciona una nueva (evitar re-codificar en updates)
        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().startsWith("$2a$")) { // O chequeo más robusto
             usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        // Asegurar estado inicial
        if (usuario.getActivo() == null) usuario.setActivo(true);
        if (usuario.getBloqueado() == null) usuario.setBloqueado(false);
        if (usuario.getIntentosFallidos() == null) usuario.setIntentosFallidos(0);

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario actualizado) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        // Actualizar campos permitidos (evitar actualizar todo ciegamente)
        if (actualizado.getPersona() != null && actualizado.getPersona().getId() != null) {
             // Aquí deberías cargar la Persona y asignarla, no confiar ciegamente en el ID
             // existente.setPersona(personaRepository.findById(actualizado.getPersona().getId()).orElse(null));
             // Por ahora, asumimos que Persona se actualiza por separado o viene completa
             existente.setPersona(actualizado.getPersona()); // Riesgoso si no se valida
        }
        if (actualizado.getUsername() != null) existente.setUsername(actualizado.getUsername());

        // Actualizar contraseña SOLO si se proporciona una nueva y no vacía
        if (actualizado.getPasswordHash() != null && !actualizado.getPasswordHash().isEmpty() && !passwordEncoder.matches(actualizado.getPasswordHash(), existente.getPasswordHash())) {
             if(!actualizado.getPasswordHash().startsWith("$2a$")) { // Solo codificar si no está codificada
                  existente.setPasswordHash(passwordEncoder.encode(actualizado.getPasswordHash()));
             } else {
                  // Podría ser un intento de poner un hash directamente? Mejor ignorar o validar.
                  System.err.println("Advertencia: Se intentó actualizar con un hash existente?");
             }
        }

        if (actualizado.getUltimoLogin() != null) existente.setUltimoLogin(actualizado.getUltimoLogin());
        if (actualizado.getBloqueado() != null) existente.setBloqueado(actualizado.getBloqueado());
        if (actualizado.getIntentosFallidos() != null) existente.setIntentosFallidos(actualizado.getIntentosFallidos());
        if (actualizado.getActivo() != null) existente.setActivo(actualizado.getActivo());
        // Actualizar roles podría ser más complejo (añadir/quitar), por ahora reemplazamos
        if (actualizado.getRoles() != null) existente.setRoles(actualizado.getRoles());

        existente.setActualizadoEn(LocalDateTime.now());
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        // Considerar lógica adicional: ¿desactivar en lugar de borrar? ¿Borrar tokens?
        usuarioRepository.deleteById(id);
    }

    // Método para listar entidades (puede ser usado internamente)
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

     // *** NUEVO: Método para listar DTOs ***
     @Transactional(readOnly = true)
     public List<UsuarioResponseDTO> listarDTOs() {
          return usuarioRepository.findAll().stream()
               .map(this::mapUsuarioToDTO)
               .collect(Collectors.toList());
     }

    // Método para obtener entidad (puede ser usado internamente)
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

     // *** NUEVO: Método para obtener DTO por ID ***
     @Transactional(readOnly = true)
     public UsuarioResponseDTO obtenerDTOPorId(Long id) {
          Usuario usuario = obtenerPorId(id); // Reutiliza el método existente
          return mapUsuarioToDTO(usuario);
     }

     // *** NUEVO: Método para obtener DTO por Persona ID ***
     @Transactional(readOnly = true)
     public UsuarioResponseDTO obtenerDTOPorPersonaId(Long personaId) {
          Usuario usuario = usuarioRepository.findByPersonaId(personaId)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado para la persona ID: " + personaId));
          return mapUsuarioToDTO(usuario);
     }

    // *** NUEVO: Método helper para mapear Entidad a DTO ***
    public UsuarioResponseDTO mapUsuarioToDTO(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setActivo(usuario.getActivo());

        // Mapear datos de Persona asociada (cargada por ser EAGER o dentro de Tx)
        Persona persona = usuario.getPersona();
        if (persona != null) {
            dto.setPersonaId(persona.getId());

            // *** CORRECCIÓN AQUÍ para nombrePersona ***
            // Construir nombre completo manejando nulos
            String nombre = persona.getNombre() != null ? persona.getNombre() : "";
            String apellido = persona.getApellido() != null ? persona.getApellido() : "";
            // Usar Stream para filtrar partes vacías y unir con espacio
            dto.setNombrePersona(
                 Stream.of(nombre, apellido)
                       .filter(s -> s != null && !s.trim().isEmpty()) // Filtrar nulos y vacíos
                       .collect(Collectors.joining(" ")) // Unir con espacio
            );
            // Si después de unir sigue vacío, poner N/A
            if (dto.getNombrePersona().isEmpty()) {
                 dto.setNombrePersona("N/A");
            }


            dto.setEmailPersona(persona.getCorreo()); // EmailPersona es redundante si ya tenemos correo
            // Mapear campos detallados de Persona
            dto.setNombre(persona.getNombre());
            dto.setApellido(persona.getApellido());
            dto.setCorreo(persona.getCorreo());
            dto.setTelefono(persona.getTelefono());
            dto.setFechaNacimiento(persona.getFechaNacimiento() != null ? persona.getFechaNacimiento().format(DATE_FORMATTER) : null);
            dto.setSexo(persona.getSexo());
            dto.setEstadoCivil(persona.getEstadoCivil());
            dto.setDireccion(persona.getDireccion());
            dto.setTelefonoEmergencia(persona.getTelefonoEmergencia());
            dto.setNotas(persona.getNotas());
        } else {
             // Si no hay persona asociada (debería ser raro si es Not Null)
             dto.setNombrePersona("Persona no asociada");
             dto.setEmailPersona("N/A");
        }

        // Mapear nombres de Roles (cargados por ser EAGER o dentro de Tx)
        if (usuario.getRoles() != null) {
             dto.setRoles(usuario.getRoles().stream()
                  .map(Rol::getNombre) // Asume getter getNombre() en Rol
                  .filter(nombreRol -> nombreRol != null && !nombreRol.isEmpty()) // Asegurar que no haya roles nulos/vacíos
                  .collect(Collectors.toList()));
        } else {
             dto.setRoles(Collections.emptyList());
        }


        return dto;
    }
}

