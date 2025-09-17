package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "cliente",
        indexes = { @Index(name="uk_cliente_codigo", columnList = "codigo", unique = true) })
public class Cliente {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "persona_id", nullable = false, unique = true)
  private Persona persona;

  @Column(length = 50, unique = true)
  private String codigo;

  @Column(name = "fecha_inicio")
  private LocalDate fechaInicio = LocalDate.now();

  @Column(length = 30)
  private String estado;

  @Column(nullable = false)
  private Boolean activo = true;

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @Column(name = "actualizado_en", nullable = false)
  private LocalDateTime actualizadoEn;

  @PrePersist
  void prePersist() {
    this.creadoEn = this.actualizadoEn = LocalDateTime.now();
    if (activo == null) activo = true;
  }

  @PreUpdate
  void preUpdate() { this.actualizadoEn = LocalDateTime.now(); }
}
