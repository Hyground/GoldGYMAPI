package com.goldgym.api.services;

import com.goldgym.api.dto.request.ClienteRequestDTO;
import com.goldgym.api.dto.response.ClienteResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.entities.Persona; // Importar Persona
import com.goldgym.api.entities.Usuario; // Importar Usuario
import com.goldgym.api.repository.ClienteRepository;
import com.goldgym.api.repository.PersonaRepository; // Importar PersonaRepository
import com.goldgym.api.repository.UsuarioRepository; // Importar UsuarioRepository
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter; // Formateador
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate; // Importar LocalDate

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository; // Inyectar repo de Persona
    private final UsuarioRepository usuarioRepository; // Inyectar repo de Usuario
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD

    // Crear cliente (asumiendo que viene entidad o se crea Persona antes)
    public Cliente crear(Cliente cliente) {
        // Lógica para asegurar que Persona exista o crearla si es necesario
        // ...
        // Generar código cliente si es necesario
        // ...
        return clienteRepository.save(cliente);
    }

    // Actualizar usando DTO
     public Cliente actualizar(Long id, ClienteRequestDTO clienteRequestDTO) {
          Cliente existente = clienteRepository.findById(id)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

          // Actualizar datos de Persona asociada
          if (clienteRequestDTO.getPersona() != null && existente.getPersona() != null) {
               Persona personaExistente = existente.getPersona();
               ClienteRequestDTO.PersonaDTO personaDTO = clienteRequestDTO.getPersona();

               // Mapear campos de PersonaDTO a Persona existente
               personaExistente.setNombre(personaDTO.getNombre());
               personaExistente.setApellido(personaDTO.getApellido());
               personaExistente.setCorreo(personaDTO.getCorreo());
               personaExistente.setTelefono(personaDTO.getTelefono());
               try { // Parsear fecha
                    personaExistente.setFechaNacimiento(personaDTO.getFechaNacimiento() != null ? LocalDate.parse(personaDTO.getFechaNacimiento(), DATE_FORMATTER) : null);
               } catch (Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de fecha de nacimiento inválido (YYYY-MM-DD)"); }
               personaExistente.setSexo(personaDTO.getSexo());
               personaExistente.setEstadoCivil(personaDTO.getEstadoCivil());
               personaExistente.setDireccion(personaDTO.getDireccion());
               personaExistente.setTelefonoEmergencia(personaDTO.getTelefonoEmergencia());
               personaExistente.setNotas(personaDTO.getNotas());
               // No guardar Persona aquí, se guarda en cascada con Cliente si la relación está bien configurada
          }

          // Actualizar datos de Cliente
          try { // Parsear fecha
               existente.setFechaInicio(clienteRequestDTO.getFechaInicio() != null ? LocalDate.parse(clienteRequestDTO.getFechaInicio(), DATE_FORMATTER) : null);
          } catch (Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de fecha de inicio inválido (YYYY-MM-DD)"); }
          // Actualizar 'activo' o 'estado' si es necesario
          // existente.setActivo(clienteRequestDTO.getActivo());

          return clienteRepository.save(existente);
     }


    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        // Considerar desactivar en lugar de borrar?
        clienteRepository.deleteById(id);
    }

    // Listar DTOs
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream()
            .map(this::mapClienteToDTO)
            .collect(Collectors.toList());
    }

     // Obtener Entidad (uso interno)
     @Transactional(readOnly = true)
     public Cliente obtenerPorId(Long id) {
         return clienteRepository.findById(id)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
     }

    // Obtener DTO por ID
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerClienteDTOPorId(Long id) {
        Cliente cliente = obtenerPorId(id); // Reutiliza búsqueda
        return mapClienteToDTO(cliente);
    }

     // Obtener DTO por Username (para perfil cliente)
     @Transactional(readOnly = true)
     public ClienteResponseDTO obtenerClientePorUsername(String username) {
          // 1. Buscar Usuario por username
          Usuario usuario = usuarioRepository.findByUsername(username)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado para el perfil"));
          // 2. Obtener Persona asociada
          Persona persona = usuario.getPersona();
          if (persona == null) {
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Usuario no tiene persona asociada");
          }
          // 3. Buscar Cliente por Persona
          Cliente cliente = clienteRepository.findByPersonaId(persona.getId())
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de cliente no encontrado"));

          // 4. Mapear a DTO
          return mapClienteToDTO(cliente);
     }


    // Buscar clientes para Modal Pagos
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarClientesPorQuery(String query) {
        String likeQuery = "%" + query.toLowerCase() + "%";
        // Asume que tienes este método en ClienteRepository
        List<Cliente> clientes = clienteRepository.buscarPorNombreApellidoCodigoCorreo(likeQuery);
        return clientes.stream()
            .map(this::mapClienteToDTO)
            .collect(Collectors.toList());
    }

    // Helper para mapear Cliente a DTO
    public ClienteResponseDTO mapClienteToDTO(Cliente cliente) {
        if (cliente == null) return null;
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setCodigoCliente(cliente.getCodigo());
        dto.setActivo(cliente.getActivo());
        dto.setFechaInicio(cliente.getFechaInicio() != null ? cliente.getFechaInicio().format(DATE_FORMATTER) : null);

        Persona persona = cliente.getPersona();
        if (persona != null) {
            dto.setPersonaId(persona.getId()); // <-- Incluir ID Persona
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

            // Campos combinados
            dto.setNombrePersona(((persona.getNombre() != null ? persona.getNombre() : "") + " " + (persona.getApellido() != null ? persona.getApellido() : "")).trim());
            dto.setEmailPersona(persona.getCorreo());
        } else {
             // Manejar caso donde cliente no tenga persona? Poco probable si es FK Not Null
             dto.setNombrePersona("Persona no asociada");
             dto.setEmailPersona("N/A");
        }
        return dto;
    }
}
