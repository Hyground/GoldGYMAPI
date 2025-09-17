package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "detalle_venta",
        indexes = { @Index(name="idx_detalle_venta_venta", columnList = "venta_id") })
public class DetalleVenta {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "venta_id", nullable = false)
  private Venta venta;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "producto_id", nullable = false)
  private Producto producto;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal cantidad;

  @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
  private BigDecimal precioUnitario;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal subtotal;
}
