package com.goldgym.api.dto.response;

import lombok.Data;

@Data
public class ClienteResponseDTO {

    // Campos de Cliente
    private Long id;
    private String codigoCliente; // Cambiado de 'codigo' para claridad
    private Boolean activo;
    private String fechaInicio; // Como String YYYY-MM-DD

    // --- ID de Persona ---
    private Long personaId; // <-- AÑADIDO

    // --- Campos de Persona (Planos) ---
    private String nombre;
    private String apellido;
    private String correo; // (emailPersona ya no es necesario si tenemos correo)
    private String telefono;
    private String fechaNacimiento; // Como String YYYY-MM-DD
    private String sexo;
    private String estadoCivil;
    private String direccion;
    private String telefonoEmergencia;
    private String notas;

    // --- Campos Combinados (Opcional, si la UI los necesita así) ---
    private String nombrePersona; // Nombre completo (calculado en el Service)
    private String emailPersona; // Email (calculado en el Service, igual a correo)

}
