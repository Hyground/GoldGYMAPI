package com.goldgym.api.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VentaDTO {
  private Long id;
  private LocalDateTime fechaVenta;
  private Long clienteId;
  private String clienteNombre;
  private Long usuarioId;
  private String usuarioUsername;
  private BigDecimal total;
  private List<DetalleVentaDTO> detalles;

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
  public static class DetalleVentaDTO {
    private Long productoId;
    private String productoNombre;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
  }
}
