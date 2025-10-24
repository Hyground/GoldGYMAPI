package com.goldgym.api.services;

import com.goldgym.api.dto.request.ClienteRequestDTO;
import com.goldgym.api.dto.response.ClienteResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.entities.Persona;
import com.goldgym.api.repository.ClienteRepository;
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
    private final PersonaService personaService; // Asumimos que PersonaService existe y puede manejar la actualización de Persona

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
            // No llamamos a personaService.actualizar, JPA se encarga por la transacción
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
        return clienteRepository.findById(personaId);
    }

    private ClienteResponseDTO convertToDto(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setCodigoCliente(cliente.getCodigo());
        dto.setActivo(cliente.getActivo());
        if (cliente.getPersona() != null) {
            dto.setNombrePersona(cliente.getPersona().getNombre() + " " + cliente.getPersona().getApellido());
            dto.setEmailPersona(cliente.getPersona().getCorreo());
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }
}