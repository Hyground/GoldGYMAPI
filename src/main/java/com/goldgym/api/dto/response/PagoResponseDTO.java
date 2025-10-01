package com.goldgym.api.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PagoResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private Long membresiaId;
    private Double monto;
    private String metodo;
    private LocalDateTime fecha;
}