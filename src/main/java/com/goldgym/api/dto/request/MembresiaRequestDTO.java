package com.goldgym.api.dto.request;

import lombok.Data;

@Data
public class MembresiaRequestDTO {
    private Long clienteId;
    private String tipo;
    private Integer duracionMeses;
    private Double costo;
}