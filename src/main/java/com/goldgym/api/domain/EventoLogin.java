package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "evento_login")
public class EventoLogin {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(nullable = false)
  private Boolean exito;

  private String ip;

  @Column(name = "user_agent", columnDefinition = "text")
  private String userAgent;

  @Column(columnDefinition = "text")
  private String detalle;

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @PrePersist
  void prePersist() { this.creadoEn = LocalDateTime.now(); }
}
