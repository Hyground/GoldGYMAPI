package com.goldgym.api.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmpleadoResponseDTO {
    private Long id;
    private String puesto; // Añade este campo a tu entidad Empleado si lo necesitas
    private String nombrePersona;
    private String correoPersona;
    private Double salario;
    private LocalDate fechaContratacion;
    private Boolean activo;
}
