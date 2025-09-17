package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "venta")
public class Venta {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fecha_venta", nullable = false)
  private LocalDateTime fechaVenta;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id")
  private Cliente cliente; // opcional

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id")
  private Usuario usuario; // quién registró

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal total = BigDecimal.ZERO;

  @Column(columnDefinition = "text")
  private String notas;

  @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DetalleVenta> detalles;

  @PrePersist
  void prePersist() { this.fechaVenta = LocalDateTime.now(); }
}
