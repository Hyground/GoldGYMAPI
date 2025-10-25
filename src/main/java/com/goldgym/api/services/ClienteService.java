package com.goldgym.api.services;

import com.goldgym.api.dto.request.ClienteRequestDTO;
import com.goldgym.api.dto.response.ClienteResponseDTO;
import com.goldgym.api.entities.Cliente;
import com.goldgym.api.entities.Persona;
import com.goldgym.api.repository.ClienteRepository;
import com.goldgym.api.repository.PersonaRepository; // Necesario para actualizar Persona
import com.goldgym.api.repository.UsuarioRepository; // Necesario para buscar por username
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ¡Importante!
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
// --- IMPORTACIÓN AÑADIDA ---
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional // Aplica transacción a todos los métodos públicos por defecto
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository; // Inyectar para manejo de Persona
    private final UsuarioRepository usuarioRepository; // Para buscar por username

    // Método original (podría eliminarse o mantenerse para uso interno)
    public Cliente crear(Cliente cliente) {
        cliente.setCreadoEn(LocalDateTime.now());
        // Aquí podrías generar el código de cliente si es necesario
        // cliente.setCodigo(...);
        cliente.setActivo(true); // Asegurar que se cree activo
        return clienteRepository.save(cliente);
    }

    // Actualizar usando DTO
    public Cliente actualizar(Long id, ClienteRequestDTO dto) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        // Actualizar datos de Persona si vienen en el DTO
        if (dto.getPersona() != null) {
            Persona persona = existente.getPersona();
            if (persona == null && dto.getPersona().getId() != null) {
                 // Intentar cargar persona si no estaba asociada (raro pero posible)
                 persona = personaRepository.findById(dto.getPersona().getId())
                     .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Persona asociada no encontrada"));
                 existente.setPersona(persona);
            } else if (persona == null) {
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede actualizar Cliente sin Persona asociada.");
            }

            // Actualizar campos de Persona
            persona.setNombre(dto.getPersona().getNombre());
            persona.setApellido(dto.getPersona().getApellido());
            persona.setCorreo(dto.getPersona().getCorreo());
            persona.setTelefono(dto.getPersona().getTelefono());
            persona.setFechaNacimiento(parseOptionalDate(dto.getPersona().getFechaNacimiento()));
            persona.setSexo(dto.getPersona().getSexo());
            persona.setEstadoCivil(dto.getPersona().getEstadoCivil());
            persona.setDireccion(dto.getPersona().getDireccion());
            persona.setTelefonoEmergencia(dto.getPersona().getTelefonoEmergencia());
            persona.setNotas(dto.getPersona().getNotas());
            persona.setActualizadoEn(LocalDateTime.now());
            // Guardar cambios en Persona (si es necesario manejarla separadamente)
            // personaRepository.save(persona); // Opcional si la cascada no está configurada
        }

        // Actualizar datos de Cliente
        existente.setFechaInicio(parseOptionalDate(dto.getFechaInicio()));
        // existente.setActivo(...); // Podrías añadir lógica para activar/desactivar
        existente.setActualizadoEn(LocalDateTime.now());

        return clienteRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        // Considerar lógica adicional (ej: desactivar en lugar de borrar,
        // verificar dependencias en Membresias, Pagos, etc.)
        clienteRepository.deleteById(id);
    }

    // --- MÉTODOS MODIFICADOS/NUEVOS PARA USAR DTOs ---

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream()
                .map(this::mapClienteToDTO) // Mapear cada cliente a DTO
                .collect(Collectors.toList());
    }

    // Método original que devuelve entidad (para uso interno si es necesario)
    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    // NUEVO: Método que devuelve DTO
    @Transactional(readOnly = true) // Necesario para acceder a datos LAZY de Persona
    public ClienteResponseDTO obtenerClienteDTOPorId(Long id) {
        Cliente cliente = obtenerPorId(id); // Reutiliza el método anterior
        return mapClienteToDTO(cliente);
    }

    // NUEVO: Buscar cliente por username (para /profile/me)
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerClientePorUsername(String username) {
        // Busca el Usuario, luego la Persona, luego el Cliente
        return usuarioRepository.findByUsername(username)
                 // Usa flatMap para manejar Optional<Optional<Cliente>>
                .flatMap(usuario -> clienteRepository.findByPersona(usuario.getPersona()))
                .map(this::mapClienteToDTO) // Mapea el Cliente encontrado (dentro del Optional) a DTO
                .orElse(null); // Devuelve null si no se encuentra Usuario o Cliente asociado
    }

     // NUEVO: Método para búsqueda por query
     @Transactional(readOnly = true)
     public List<ClienteResponseDTO> buscarClientesPorQuery(String query) {
          // Necesitas añadir el método findByCriteria en ClienteRepository
          List<Cliente> clientes = clienteRepository.findByNombreOrApellidoOrCodigoOrCorreoContainingIgnoreCase(query);
          return clientes.stream()
               .map(this::mapClienteToDTO)
               .collect(Collectors.toList());
     }

    // --- HELPER METHODS ---

    // Método helper privado para mapear Entidad Cliente a ClienteResponseDTO
    private ClienteResponseDTO mapClienteToDTO(Cliente cliente) {
        if (cliente == null) return null;

        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setCodigoCliente(cliente.getCodigo());
        dto.setActivo(cliente.getActivo());
        if (cliente.getFechaInicio() != null) {
            dto.setFechaInicio(cliente.getFechaInicio().toString()); // Convertir LocalDate a String
        }

        // Acceder a Persona DENTRO de la transacción
        Persona persona = cliente.getPersona(); // Hibernate cargará el proxy aquí si es necesario
        if (persona != null) {
            // Campos combinados para la lista principal
            dto.setNombrePersona((persona.getNombre() != null ? persona.getNombre() : "") + " " + (persona.getApellido() != null ? persona.getApellido() : ""));
            dto.setEmailPersona(persona.getCorreo());

            // Campos detallados individuales
            dto.setNombre(persona.getNombre());
            dto.setApellido(persona.getApellido());
            dto.setCorreo(persona.getCorreo());
            dto.setTelefono(persona.getTelefono());
            if (persona.getFechaNacimiento() != null) {
                dto.setFechaNacimiento(persona.getFechaNacimiento().toString()); // Convertir LocalDate a String
            }
            dto.setSexo(persona.getSexo());
            dto.setEstadoCivil(persona.getEstadoCivil());
            dto.setDireccion(persona.getDireccion());
            dto.setTelefonoEmergencia(persona.getTelefonoEmergencia());
            dto.setNotas(persona.getNotas());
        } else {
             // Manejar caso donde Persona podría ser null inesperadamente
             dto.setNombrePersona("Persona no asignada");
             dto.setEmailPersona("N/A");
        }
        return dto;
    }

     // Helper para parsear fechas opcionales de String a LocalDate
     private LocalDate parseOptionalDate(String dateString) {
          if (dateString == null || dateString.trim().isEmpty()) {
               return null;
          }
          try {
               return LocalDate.parse(dateString);
          } catch (DateTimeParseException e) {
               // Loggear el error o manejarlo como prefieras
               System.err.println("Error parseando fecha: " + dateString + " - " + e.getMessage());
               return null; // O lanzar excepción si la fecha es mandatoria y mal formateada
          }
     }
}

