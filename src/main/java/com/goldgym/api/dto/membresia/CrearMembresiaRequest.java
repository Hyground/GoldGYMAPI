package com.goldgym.api.dto.membresia;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CrearMembresiaRequest {
  private UUID clienteId;
  private UUID planId;
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private BigDecimal precioContratado;
}
