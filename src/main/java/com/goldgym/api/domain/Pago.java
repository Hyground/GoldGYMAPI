package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "pago",
        indexes = {
          @Index(name="idx_pago_membresia", columnList = "membresia_id"),
          @Index(name="idx_pago_cliente", columnList = "cliente_id")
        })
public class Pago {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "membresia_id", nullable = false)
  private Membresia membresia;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal monto;

  @Column(length = 10, nullable = false)
  private String moneda = "GTQ";

  @Column(name = "metodo_pago", length = 30, nullable = false)
  private String metodoPago;

  @Column(name = "pagado_en", nullable = false)
  private LocalDateTime pagadoEn;

  @Column(length = 120)
  private String referencia;

  @Column(columnDefinition = "text")
  private String notas;

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @Column(name = "actualizado_en", nullable = false)
  private LocalDateTime actualizadoEn;

  @PrePersist
  void prePersist() {
    this.creadoEn = this.actualizadoEn = this.pagadoEn = LocalDateTime.now();
    if (moneda == null) moneda = "GTQ";
  }

  @PreUpdate
  void preUpdate() { this.actualizadoEn = LocalDateTime.now(); }
}
