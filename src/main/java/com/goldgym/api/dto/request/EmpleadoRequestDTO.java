package com.goldgym.api.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EmpleadoRequestDTO {

    private PersonaDTO persona;
    private Double salario;
    private String fechaContratacion;

    @Getter
    @Setter
    public static class PersonaDTO {
        private Long id;
        private String nombre;
        private String apellido;
        private String correo;
        private String telefono;
        private String fechaNacimiento;
        private String sexo;
        private String estadoCivil;
        private String direccion;
        private String telefonoEmergencia;
        private String notas;
    }
}