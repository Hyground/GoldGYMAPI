package com.goldgym.api.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequestDTO {
    private Long clienteId;
    private List<Long> productosIds;
    private Double total;
}