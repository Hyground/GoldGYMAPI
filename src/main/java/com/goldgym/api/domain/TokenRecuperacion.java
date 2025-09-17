package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "token_recuperacion")
public class TokenRecuperacion {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(length = 30)
  private String canal; // email, sms, etc.

  @Column(length = 120)
  private String destino;

  @Column(length = 20)
  private String codigo;

  @Column(length = 120)
  private String token;

  @Column(name = "expira_en", nullable = false)
  private LocalDateTime expiraEn;

  @Column(name = "usado_en")
  private LocalDateTime usadoEn;

  @Column(nullable = false)
  private Integer intento = 0;

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @PrePersist
  void prePersist() {
    this.creadoEn = LocalDateTime.now();
    if (intento == null) intento = 0;
  }
}
