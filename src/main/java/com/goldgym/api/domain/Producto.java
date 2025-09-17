package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "producto",
        indexes = { @Index(name="idx_producto_nombre", columnList = "nombre") })
public class Producto {

  public enum TipoMedida { UNIDAD, SCOOP }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 120, nullable = false)
  private String nombre;

  @Column(length = 60, nullable = false)
  private String categoria;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_medida", length = 10, nullable = false)
  private TipoMedida tipoMedida;

  private Integer scoopsPorEnvase;

  @Column(name = "precio_venta", precision = 10, scale = 2, nullable = false)
  private BigDecimal precioVenta;

  @Column(name = "stock_cantidad", precision = 12, scale = 2, nullable = false)
  private BigDecimal stockCantidad = BigDecimal.ZERO;

  @Column(name = "stock_minimo_alerta", precision = 12, scale = 2, nullable = false)
  private BigDecimal stockMinimoAlerta = BigDecimal.valueOf(5);

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
