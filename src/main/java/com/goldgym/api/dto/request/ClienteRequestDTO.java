package com.goldgym.api.dto.request;

import lombok.Data;

@Data
public class ClienteRequestDTO {
    private Long personaId;  // relación a Persona
    private String codigoCliente;
    private Boolean activo;
}
