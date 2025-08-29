package com.goldgym.api.dto.cliente;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CrearClienteRequest {
  private String nombres;
  private String apellidos;
  private LocalDate fechaNacimiento;
  private String sexo;
  private String telefono;
  private String correo;
  private String direccion;
  private String codigo; // opcional
}
