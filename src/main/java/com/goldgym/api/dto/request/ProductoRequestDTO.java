package com.goldgym.api.dto.request;

import lombok.Data;

@Data
public class ProductoRequestDTO {
    private String nombre;
    private Double precio;
    private Integer stock;
}