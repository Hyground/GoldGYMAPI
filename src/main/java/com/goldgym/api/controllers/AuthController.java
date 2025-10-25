package com.goldgym.api.controllers;

import com.goldgym.api.dto.auth.LoginRequest;
import com.goldgym.api.dto.auth.LoginResponse;
import com.goldgym.api.entities.Cliente; // Importar Cliente
import com.goldgym.api.entities.Empleado; // Importar Empleado
import com.goldgym.api.jwt.JwtUtil;
import com.goldgym.api.entities.Usuario;
import com.goldgym.api.repository.ClienteRepository; // Importar Repo Cliente
import com.goldgym.api.repository.EmpleadoRepository; // Importar Repo Empleado
import com.goldgym.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException; // Importar DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional; // Importar Optional
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService; // Para validar credenciales y estado inicial
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository; // Para obtener Usuario completo (ID, activo)
    private final ClienteRepository clienteRepository; // Para buscar clienteId
    private final EmpleadoRepository empleadoRepository; // Para buscar empleadoId

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Autenticar (verifica contraseña)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // 2. Cargar UserDetails (verifica si existe y carga roles)
            // UserDetailsService (UsuarioService) ahora lanza excepción si está inactivo
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            // 3. Obtener Usuario completo (para ID)
            Usuario usuarioCompleto = usuarioRepository.findByUsername(loginRequest.getUsername())
                 .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado post-auth")); // No debería pasar

            // 4. Generar Token
            final String token = jwtUtil.generateToken(userDetails);

            // 5. Extraer roles
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.toList());

            // 6. Obtener clienteId y empleadoId (si aplican)
            Long clienteId = null;
            Long empleadoId = null;
            if (usuarioCompleto.getPersona() != null) {
                Long personaId = usuarioCompleto.getPersona().getId();
                if (roles.contains("CLIENTE")) {
                    Optional<Cliente> clienteOpt = clienteRepository.findByPersonaId(personaId);
                    clienteId = clienteOpt.map(Cliente::getId).orElse(null);
                    if (clienteId == null) System.err.println("Advertencia: Usuario con rol CLIENTE no tiene registro Cliente asociado a Persona ID " + personaId);
                }
                if (roles.contains("EMPLEADO")) {
                    Optional<Empleado> empleadoOpt = empleadoRepository.findByPersonaId(personaId);
                    empleadoId = empleadoOpt.map(Empleado::getId).orElse(null);
                    if (empleadoId == null) System.err.println("Advertencia: Usuario con rol EMPLEADO no tiene registro Empleado asociado a Persona ID " + personaId);
                }
            } else {
                 System.err.println("Advertencia: Usuario " + usuarioCompleto.getUsername() + " no tiene Persona asociada.");
            }

            // 7. Retornar LoginResponse COMPLETO
            return new LoginResponse(token, roles, userDetails.getUsername(), usuarioCompleto.getId(), clienteId, empleadoId);

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", e);
        } catch (DisabledException | UsernameNotFoundException e) {
             // Captura tanto el no encontrado como el inactivo (si UserDetailsService lanza UsernameNotFoundException para inactivo)
             // O captura DisabledException si UserDetailsService la lanza explícitamente
             // Ajusta el mensaje según la excepción real que lance tu UserDetailsService para inactivos
             if (e.getMessage().contains("inactiva")) {
                   throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cuenta de usuario está inactiva.", e);
             } else {
                   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", e); // O "Usuario no encontrado"
             }
        } catch (Exception e) {
            System.err.println("Error inesperado durante el login: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al procesar el login", e);
        }
    }
}
