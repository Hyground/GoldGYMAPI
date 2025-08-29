package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario implements UserDetails {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @PrePersist
  void pre() {
    if (id == null) id = UUID.randomUUID();
    if (rol == null || rol.isBlank()) rol = "cliente";   // safety por si no viene en builder
    if (activo == null) activo = true;
  }

  @OneToOne(optional = false)
  @JoinColumn(name = "persona_id", unique = true)
  private Persona persona;

  @Column(unique = true)
  private String username;                // correo o alias

  @Column(name = "password_hash", nullable = false)
  private String password;                // hash (BCrypt)

  @Builder.Default
  private String rol = "cliente";

  @Builder.Default
  private Boolean activo = true;

  @Override public String getUsername() { return username; }
  @Override public String getPassword() { return password; }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String r = (rol == null || rol.isBlank()) ? "CLIENTE" : rol.toUpperCase();
    return List.of(new SimpleGrantedAuthority("ROLE_" + r));
  }

  @Override public boolean isAccountNonExpired() { return true; }
  @Override public boolean isAccountNonLocked() { return true; }
  @Override public boolean isCredentialsNonExpired() { return true; }
  @Override public boolean isEnabled() { return Boolean.TRUE.equals(activo); }
}
