package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "medicion",
        indexes = { @Index(name="idx_medicion_cliente_fecha", columnList = "cliente_id, fecha_medicion DESC") })
public class Medicion {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @Column(name = "fecha_medicion", nullable = false)
  private LocalDate fechaMedicion = LocalDate.now();

  private BigDecimal pesoKg;
  private BigDecimal alturaCm;
  private BigDecimal brazoCm;
  private BigDecimal piernaCm;
  private BigDecimal cinturaCm;
  private BigDecimal cuelloCm;

  @Column(columnDefinition = "text")
  private String notas;

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @Column(name = "actualizado_en", nullable = false)
  private LocalDateTime actualizadoEn;

  @PrePersist
  void prePersist() {
    this.creadoEn = this.actualizadoEn = LocalDateTime.now();
  }

  @PreUpdate
  void preUpdate() { this.actualizadoEn = LocalDateTime.now(); }
}
