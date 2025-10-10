package com.goldgym.api.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientePagoStatusDTO {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private String codigoCliente;
    private String estadoPago;
    private LocalDate fechaVencimiento;
    private Double montoPendiente; // Podría ser útil
}
