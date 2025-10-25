package com.goldgym.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private List<String> roles;
    private String username;
    private Long userId; // ID del Usuario
    private Long clienteId; // ID del Cliente (si aplica)
    private Long empleadoId; // ID del Empleado (si aplica)
}
