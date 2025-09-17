package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "asistencia",
        uniqueConstraints = { @UniqueConstraint(name="uk_asistencia_cliente_fecha", columnNames = {"cliente_id","fecha"}) })
public class Asistencia {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @Column(nullable = false)
  private LocalDate fecha = LocalDate.now();

  @Column(nullable = false)
  private Boolean presente = true;

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @PrePersist
  void prePersist() {
    this.creadoEn = LocalDateTime.now();
    if (presente == null) presente = true;
  }
}
