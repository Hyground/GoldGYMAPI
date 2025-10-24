package com.goldgym.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List; // Importar List

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private List<String> roles; // NUEVO CAMPO
    private String username;
}