package com.goldgym.api.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private List<String> productos;
    private Double total;
    private LocalDateTime fecha;
}