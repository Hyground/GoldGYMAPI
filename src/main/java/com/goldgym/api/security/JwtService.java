package com.goldgym.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

  private final Key key;
  private final long expirationMs;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration-minutes:120}") long expMinutes
  ) {
    // Asegúrate que el secret tenga al menos 32 bytes (256 bits) para HS256.
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expMinutes * 60_000;
  }

  /* ========================
      GENERACIÓN DE TOKENS
  ========================= */
  public String generate(String subject) {
    return generate(subject, Map.of());
  }

  public String generate(String subject, Map<String, Object> extraClaims) {
    long now = System.currentTimeMillis();
    Date iat = new Date(now);
    Date exp = new Date(now + expirationMs);

    return Jwts.builder()
        .setSubject(subject)
        .setClaims(extraClaims)
        .setIssuedAt(iat)
        .setExpiration(exp)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  /* ========================
        PARSEO / CLAIMS
  ========================= */
  public Claims getAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String getSubject(String token) {
    return getAllClaims(token).getSubject();
  }

  public Date getExpiration(String token) {
    return getAllClaims(token).getExpiration();
  }

  /* ========================
          VALIDACIÓN
  ========================= */
  /** Valida firma y expiración. */
  public boolean isValid(String token) {
    try {
      // parseClaimsJws valida firma y fecha de expiración por defecto
      getAllClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  /** Valida firma/expiración y que el subject coincida. */
  public boolean isValid(String token, String expectedSubject) {
    try {
      Claims c = getAllClaims(token);
      return expectedSubject != null && expectedSubject.equals(c.getSubject());
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
