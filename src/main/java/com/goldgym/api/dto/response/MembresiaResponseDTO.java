package com.goldgym.api.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MembresiaResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private String tipo;
    private Integer duracionMeses;
    private Double costo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}