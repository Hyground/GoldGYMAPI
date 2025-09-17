package com.goldgym.api.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteDTO {
  private Long id;
  private String codigo;
  private String nombre;
  private String apellido;
  private String correo;
  private String telefono;
  private LocalDate fechaInicio;
  private String estado;
}
