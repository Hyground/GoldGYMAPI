package com.goldgym.api.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanAnaliticasDTO {
    private Long id;
    private String nombrePlan;
    private Double precio; // Incluimos precio para consistencia
    private Integer duracionDias;
    private String descripcion;
    private Long clientesActivos; // ESTE ES EL CONTADOR CLAVE
}