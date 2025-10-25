package com.goldgym.api.dto.response;

import lombok.Data;
import java.util.List;

// DTO para enviar información del Usuario al frontend
@Data
public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private Boolean activo;
    private Boolean bloqueado; // Opcional, si quieres mostrarlo

    // Campos aplanados de Persona
    private Long personaId; // Importante para la edición
    private String nombrePersona; // Nombre completo para mostrar en tablas
    private String emailPersona; // Email para mostrar en tablas

    // Campos detallados de Persona (si los necesitas al editar Admin directamente)
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

    // Roles (solo nombres)
    private List<String> roles;
}
