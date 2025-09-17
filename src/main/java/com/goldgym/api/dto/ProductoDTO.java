package com.goldgym.api.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductoDTO {
  private Long id;
  private String nombre;
  private String categoria;
  private String tipoMedida; // UNIDAD o SCOOP
  private Integer scoopsPorEnvase;
  private BigDecimal precioVenta;
  private BigDecimal stockCantidad;
}
