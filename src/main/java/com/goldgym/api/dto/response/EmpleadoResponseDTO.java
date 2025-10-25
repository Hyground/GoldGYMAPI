package com.goldgym.api.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmpleadoResponseDTO {
    // Campos de Empleado
    private Long id;
    // private String puesto; // Añadir a entidad Empleado si se necesita
    private Double salario;
    private LocalDate fechaContratacion; // Mantener LocalDate aquí
    private Boolean activo;

    // --- ID de Persona ---
    private Long personaId; // <-- AÑADIDO

    // --- Campos de Persona (Planos) ---
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String fechaNacimiento; // Como String YYYY-MM-DD
    private String sexo;
    private String estadoCivil;
    private String direccion;
    private String telefonoEmergencia;
    private String notas;

    // --- Campos Combinados (Opcional) ---
    private String nombrePersona; // Nombre completo (calculado en Service)
    private String emailPersona; // Email (calculado en Service)

}
