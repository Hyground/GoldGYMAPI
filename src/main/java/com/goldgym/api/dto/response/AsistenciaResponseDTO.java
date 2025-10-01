package com.goldgym.api.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AsistenciaResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNombre; // opcional
    private LocalDate fecha;
    private Boolean presente;
    private LocalDateTime creadoEn;
}