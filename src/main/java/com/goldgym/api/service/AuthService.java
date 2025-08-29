package com.goldgym.api.service;

import com.goldgym.api.domain.Persona;
import com.goldgym.api.domain.Usuario;
import com.goldgym.api.dto.auth.*;
import com.goldgym.api.repository.PersonaRepository;
import com.goldgym.api.repository.UsuarioRepository;
import com.goldgym.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonaRepository personaRepo;
    private final UsuarioRepository usuarioRepo;
    private final JwtService jwt;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Registro de usuario
    public AuthResponse register(RegisterRequest req) {
        var persona = req.getCorreoOtelefono().contains("@")
                ? personaRepo.findByCorreoIgnoreCase(req.getCorreoOtelefono())
                : personaRepo.findByTelefono(req.getCorreoOtelefono());

        var p = persona.orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

        usuarioRepo.findByPersona_CorreoIgnoreCase(p.getCorreo())
                .ifPresent(u -> { throw new IllegalStateException("La persona ya tiene usuario"); });

        var username = (req.getUsername() != null && !req.getUsername().isBlank())
                ? req.getUsername()
                : (p.getCorreo() != null ? p.getCorreo().toLowerCase() : p.getTelefono());

        var u = Usuario.builder()
                .persona(p)
                .username(username)
                .password(encoder.encode(req.getPassword()))
                .rol(req.getRol() == null ? "cliente" : req.getRol())
                .activo(true)
                .build();

        usuarioRepo.save(u);
        return new AuthResponse(jwt.generate(u.getUsername()));
    }

    // Login flexible: correo, teléfono o username
    public AuthResponse login(LoginRequest req) {
        var id = req.getIdentificador();

        var user = usuarioRepo.findByUsername(id)
                .or(() -> usuarioRepo.findByPersona_CorreoIgnoreCase(id))
                .or(() -> usuarioRepo.findByPersona_Telefono(id))
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Credenciales inválidas");

        return new AuthResponse(jwt.generate(user.getUsername()));
    }
}
