package com.goldgym.api.services;

import com.goldgym.api.dto.request.PersonaRequestDTO;
import com.goldgym.api.entities.*;
import com.goldgym.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public Persona crear(Persona persona) {
        return personaRepository.save(persona);
    }

    @Transactional
    public void createUnified(PersonaRequestDTO dto) {
        Persona persona = new Persona();
        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setCorreo(dto.getCorreo());
        persona.setTelefono(dto.getTelefono());
        persona.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
        Persona savedPersona = personaRepository.save(persona);

        String roleName = dto.getRol().toUpperCase();

        // 2. Crear Usuario para todos los roles si se proporcionan credenciales
        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setUsername(dto.getUsername());
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            usuario.setPersona(savedPersona);
            usuario.setCreadoEn(LocalDateTime.now());
            usuario.setActivo(true);

            Rol rol = rolRepository.findByNombre(roleName)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            Set<Rol> roles = new HashSet<>();
            roles.add(rol);
            usuario.setRoles(roles);
            usuarioRepository.save(usuario);
        }

        // 3. Crear la entidad específica del rol
        switch (roleName) {
            case "CLIENTE":
                Cliente cliente = new Cliente();
                cliente.setPersona(savedPersona);
                cliente.setFechaInicio(LocalDate.parse(dto.getFechaInicio()));
                cliente.setActivo(true);
                cliente.setCreadoEn(LocalDateTime.now());
                Cliente savedCliente = clienteRepository.save(cliente);
                // Generar y guardar código de cliente
                savedCliente.setCodigo("CL-" + savedPersona.getId() + savedCliente.getId());
                clienteRepository.save(savedCliente);
                break;
            case "EMPLEADO":
                Empleado empleado = new Empleado();
                empleado.setPersona(savedPersona);
                empleado.setSalario(dto.getSalario());
                empleado.setFechaContratacion(LocalDate.parse(dto.getFechaContratacion()));
                empleado.setActivo(true);
                empleado.setCreadoEn(LocalDateTime.now());
                empleadoRepository.save(empleado);
                break;
            case "ADMINISTRADOR":
                break;
            default:
                throw new RuntimeException("Error: Rol no válido especificado.");
        }
    }

    public Persona actualizar(Long id, Persona actualizada) {
        Persona existente = personaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
        existente.setNombre(actualizada.getNombre());
        existente.setApellido(actualizada.getApellido());
        existente.setCorreo(actualizada.getCorreo());
        existente.setTelefono(actualizada.getTelefono());
        existente.setFechaNacimiento(actualizada.getFechaNacimiento());
        return personaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!personaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        personaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Persona> listar() {
        return personaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Persona obtenerPorId(Long id) {
        return personaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Persona> searchPersonas(String searchTerm) {
        // Usamos el método definido en el repositorio para buscar en 3 campos
        return personaRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCorreoContainingIgnoreCase(
            searchTerm, searchTerm, searchTerm
        );
    }
}
