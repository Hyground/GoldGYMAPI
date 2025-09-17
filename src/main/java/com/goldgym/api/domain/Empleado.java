package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "empleado",
        indexes = { @Index(name="uk_empleado_codigo", columnList = "codigo_empleado", unique = true) })
public class Empleado {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "persona_id", nullable = false, unique = true)
  private Persona persona;

  @Column(name = "codigo_empleado", length = 50, unique = true)
  private String codigoEmpleado;

  @Column(name = "fecha_contratacion")
  private LocalDate fechaContratacion;

  @Column(precision = 10, scale = 2)
  private BigDecimal salario;

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
