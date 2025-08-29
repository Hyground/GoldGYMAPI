package com.goldgym.api.security;

import com.goldgym.api.domain.Usuario;
import com.goldgym.api.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwt;
  private final UsuarioRepository usuarioRepo;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    // 1) Whitelist: todo lo de /auth/** pasa directo (login, register, etc.)
    if (req.getServletPath().startsWith("/auth/")) { chain.doFilter(req, res); return; }

    // 2) Si ya hay autenticación en el contexto, no hacemos nada
    if (SecurityContextHolder.getContext().getAuthentication() != null) { chain.doFilter(req, res); return; }

    // 3) Leer header Authorization
    String auth = req.getHeader("Authorization");
    if (auth == null || !auth.startsWith("Bearer ")) { chain.doFilter(req, res); return; }

    String token = auth.substring(7);

    try {
      // 4) Extraer el "subject" (normalmente username) del token
      String subject = jwt.getSubject(token);
      if (!jwt.isValid(token, subject)) { chain.doFilter(req, res); return; }


      // (Opcional) si tu JwtService tiene validación de expiración/firma, úsala aquí:
      // if (!jwt.isValid(token, subject)) { chain.doFilter(req, res); return; }

      // 5) Cargar el usuario y colocar Authentication en el contexto
      usuarioRepo.findByUsername(subject).ifPresent(u -> {
  var authToken = new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());
  SecurityContextHolder.getContext().setAuthentication(authToken);
});

    } catch (Exception ignored) {
      // Si hay cualquier problema con el token, no autenticamos y dejamos seguir la
      // cadena
    }

    chain.doFilter(req, res);
  }
}
