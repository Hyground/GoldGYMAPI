package com.goldgym.api.dto.response;

import lombok.Data;
import java.util.List;

/**
 * DTO para devolver información del Usuario de forma segura y eficiente,
 * incluyendo datos básicos de la Persona asociada y los roles.
 */
@Data
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private Boolean activo;
    private Long personaId; // ID de la persona asociada
    private String nombrePersona; // Nombre completo para mostrar
    private String emailPersona; // Email para mostrar
    private List<String> roles; // Nombres de los roles (ej: ["ADMINISTRADOR", "EMPLEADO"])

    // Campos adicionales de Persona si los necesitas directamente en la tabla de admin
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

}
