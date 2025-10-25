package com.goldgym.api.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// Nota: Este DTO no se usa en el EmpleadoService que te dí,
// pero sería la forma recomendada para el método actualizar.
@Data
public class EmpleadoRequestDTO {

    private PersonaDTO persona;
    private Double salario;
    private String fechaContratacion;
    // Podrías añadir 'puesto', 'activo', etc. aquí también

    @Getter
    @Setter
    public static class PersonaDTO {
        private Long id; // Importante para asociar/actualizar
        private String nombre;
        private String apellido;
        private String correo;
        private String telefono;
        private String fechaNacimiento; // String YYYY-MM-DD
        private String sexo;
        private String estadoCivil;
        private String direccion;
        private String telefonoEmergencia;
        private String notas;
    }
}
