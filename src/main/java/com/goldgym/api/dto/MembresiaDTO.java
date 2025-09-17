package com.goldgym.api.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MembresiaDTO {
  private Long id;
  private Long clienteId;
  private String clienteNombre;
  private Long planId;
  private String planNombre;
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private BigDecimal precioContratado;
  private String estado;
}
