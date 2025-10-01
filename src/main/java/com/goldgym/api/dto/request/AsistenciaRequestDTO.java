package com.goldgym.api.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AsistenciaRequestDTO {
    private Long clienteId;
    private LocalDate fecha;
    private Boolean presente;
}