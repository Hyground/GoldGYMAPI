package com.goldgym.api.dto.request;

import lombok.Data;

@Data
public class PagoRequestDTO {
    private Long clienteId;
    private Long membresiaId;
    private Double monto;
    private String metodo;
}