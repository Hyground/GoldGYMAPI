package com.goldgym.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaRequestDTO {
    // Persona fields
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String fechaNacimiento;

    // Role
    private String rol;

    // Usuario fields
    private String username;
    private String password;

    // Cliente fields
    private String fechaInicio;

    // Empleado fields
    private Double salario;
    private String fechaContratacion;
}
