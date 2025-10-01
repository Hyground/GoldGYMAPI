package com.goldgym.api.dto.response;

import lombok.Data;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String codigoCliente;
    private Boolean activo;

    // Información adicional de persona (en lugar de devolver toda la entidad)
    private String nombrePersona;
    private String emailPersona;
}