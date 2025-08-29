package com.goldgym.api.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String identificador;  // puede ser email, teléfono o username
    private String password;
}
