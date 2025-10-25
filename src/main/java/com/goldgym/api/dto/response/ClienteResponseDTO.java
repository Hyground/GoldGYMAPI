package com.goldgym.api.dto.response;

import lombok.Data;

@Data
public class ClienteResponseDTO {
    
    // Campos de Cliente
    private Long id;
    private String codigoCliente;
    private Boolean activo;
    private String fechaInicio; // Asegúrate de incluir este campo del ClienteService

    // Campos de Resumen (ya existen)
    private String nombrePersona;
    private String emailPersona;
    
    // --- CAMPOS DETALLADOS DE PERSONA (AÑADIDOS PARA EL DASHBOARD CLIENTE) ---
    // Lombok (@Data) generará los Getters y Setters CORRECTOS para estos campos.
    
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
    // --- FIN CAMPOS DETALLADOS ---
    
    // Los métodos setApellido, setTelefono, etc., han sido eliminados.
    // @Data se encargará de generarlos correctamente.
}