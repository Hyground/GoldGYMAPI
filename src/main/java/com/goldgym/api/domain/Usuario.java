package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "usuario",
        indexes = {
          @Index(name="uk_usuario_username", columnList = "username", unique = true)
        })
public class Usuario {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "persona_id", nullable = false)
  private Persona persona;

  @Column(length = 50, nullable = false, unique = true)
  private String username;

  @Column(name = "password_hash", length = 255, nullable = false)
  private String passwordHash;

  @Column(name = "ultimo_login")
  private LocalDateTime ultimoLogin;

  @Column(nullable = false)
  private Boolean bloqueado = false;

  @Column(name = "intentos_fallidos", nullable = false)
  private Integer intentosFallidos = 0;

  @Column(nullable = false)
  private Boolean activo = true;

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @Column(name = "actualizado_en", nullable = false)
  private LocalDateTime actualizadoEn;

  @PrePersist
  void prePersist() {
    this.creadoEn = this.actualizadoEn = LocalDateTime.now();
    if (bloqueado == null) bloqueado = false;
    if (activo == null) activo = true;
    if (intentosFallidos == null) intentosFallidos = 0;
  }

  @PreUpdate
  void preUpdate() { this.actualizadoEn = LocalDateTime.now(); }
}
