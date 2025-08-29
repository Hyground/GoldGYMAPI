package com.goldgym.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
  @NotBlank private String correoOtelefono;
  @NotBlank private String password;
  private String rol = "cliente"; // 'empleado' o 'admin' si aplica
  private String username;        // opcional
}
