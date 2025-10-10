package com.goldgym.api.services;

import com.goldgym.api.dto.response.ClienteResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente crear(Cliente cliente) {
        cliente.setCreadoEn(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Long id, Cliente actualizado) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        existente.setPersona(actualizado.getPersona());
        existente.setCodigo(actualizado.getCodigo());
        existente.setFechaInicio(actualizado.getFechaInicio());
        existente.setEstado(actualizado.getEstado());
        existente.setActivo(actualizado.getActivo());
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