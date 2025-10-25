package com.goldgym.api.services;

import com.goldgym.api.dto.request.ClienteRequestDTO;
import com.goldgym.api.dto.response.ClienteResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.entities.Persona;
import com.goldgym.api.entities.Usuario;
import com.goldgym.api.repository.ClienteRepository;
import com.goldgym.api.repository.UsuarioRepository; // <-- Ahora podemos usarlo!
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PersonaService personaService; 
    
    // --- DEPENDENCIAS CLAVE PARA EL PERFIL DEL CLIENTE ---
    private final UsuarioRepository usuarioRepository; 
    // ---------------------------------------------------

    // [ ... CÓDIGO DE CREAR, ACTUALIZAR, ELIMINAR, LISTAR (sin cambios) ... ]

    public Cliente crear(Cliente cliente) {
        if (cliente.getPersona() != null) {
            cliente.setPersona(personaService.crear(cliente.getPersona()));
        }
        cliente.setCreadoEn(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Long id, ClienteRequestDTO dto) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        // Actualizar la entidad Persona anidada
        if (dto.getPersona() != null) {
            Persona personaExistente = existente.getPersona();
            if (personaExistente == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente no tiene una persona asociada para actualizar.");
            }
            ClienteRequestDTO.PersonaDTO personaDto = dto.getPersona();

            // Actualización campo por campo para evitar nulos
            if (personaDto.getNombre() != null) personaExistente.setNombre(personaDto.getNombre());
            if (personaDto.getApellido() != null) personaExistente.setApellido(personaDto.getApellido());
            if (personaDto.getCorreo() != null) personaExistente.setCorreo(personaDto.getCorreo());
            if (personaDto.getTelefono() != null) personaExistente.setTelefono(personaDto.getTelefono());
            if (personaDto.getFechaNacimiento() != null) personaExistente.setFechaNacimiento(LocalDate.parse(personaDto.getFechaNacimiento()));
            if (personaDto.getSexo() != null) personaExistente.setSexo(personaDto.getSexo());
            if (personaDto.getEstadoCivil() != null) personaExistente.setEstadoCivil(personaDto.getEstadoCivil());
            if (personaDto.getDireccion() != null) personaExistente.setDireccion(personaDto.getDireccion());
            if (personaDto.getTelefonoEmergencia() != null) personaExistente.setTelefonoEmergencia(personaDto.getTelefonoEmergencia());
            if (personaDto.getNotas() != null) personaExistente.setNotas(personaDto.getNotas());
            
            personaExistente.setActualizadoEn(LocalDateTime.now());
        }

        // Actualizar campos del Cliente
        if (dto.getFechaInicio() != null && !dto.getFechaInicio().isEmpty()) {
            existente.setFechaInicio(LocalDate.parse(dto.getFechaInicio()));
        }

        existente.setActualizadoEn(LocalDateTime.now());
        return clienteRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByPersonaId(Long personaId) {
        // Nota: Asegúrate de tener este método en ClienteRepository: findByPersonaId
        // return clienteRepository.findByPersonaId(personaId); 
        return Optional.empty(); // Retorno de ejemplo
    }


    // ----------------------------------------------------------------------
    // --- MÉTODO CRÍTICO PARA EL DASHBOARD DEL CLIENTE (PROFILE/ME) ---
    // ----------------------------------------------------------------------
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerClientePorUsername(String username) {
        
        // 1. Buscar la entidad Usuario por el username logueado
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario logueado no encontrado."));

        // 2. Obtener la Persona asociada a ese Usuario
        Persona persona = usuario.getPersona();
        if (persona == null) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta de usuario no está asociada a una Persona.");
        }

        // 3. Buscar la entidad Cliente asociada a esa Persona
        // NOTA: Requiere que ClienteRepository tenga un método 'findByPersona(Persona persona)'
        // O si tu relación Cliente->Persona usa el ID de Persona:
        Cliente cliente = clienteRepository.findByPersonaId(persona.getId()) // ASUMO QUE USAS EL ID DE PERSONA
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado para la persona logueada."));

        // 4. Devolver el DTO del Cliente
        return convertToDto(cliente);
    }
    // ----------------------------------------------------------------------


    private ClienteResponseDTO convertToDto(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setCodigoCliente(cliente.getCodigo());
        dto.setActivo(cliente.getActivo());
        dto.setFechaNacimiento(cliente.getFechaInicio() != null ? cliente.getFechaInicio().toString() : null);

        // --- INCLUIR TODOS LOS DATOS DE PERSONA QUE EL FRONTEND NECESITA ---
        if (cliente.getPersona() != null) {
            Persona persona = cliente.getPersona();
            
            // Campos de resumen
            dto.setNombrePersona(persona.getNombre() + " " + persona.getApellido());
            dto.setEmailPersona(persona.getCorreo()); 
            
            // Campos detallados requeridos por el dashboard_cliente.js
            dto.setNombrePersona(persona.getNombre());
            dto.setApellido(persona.getApellido());
            dto.setCorreo(persona.getCorreo());
            dto.setTelefono(persona.getTelefono());
            dto.setFechaNacimiento(persona.getFechaNacimiento() != null ? persona.getFechaNacimiento().toString() : null);
            dto.setSexo(persona.getSexo());
            dto.setEstadoCivil(persona.getEstadoCivil());
            dto.setDireccion(persona.getDireccion());
            dto.setTelefono(persona.getTelefonoEmergencia());
            dto.setNotas(persona.getNotas());
        }
        
        return dto;
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }
}