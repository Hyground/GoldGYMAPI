package com.goldgym.api.dto.response;

import lombok.Data;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String codigoCliente;
    private Boolean activo;

    // Información adicional de persona (en lugar de devolver toda la entidad)
    private String nombrePersona;
    private String emailPersona;
    public void setApellido(String apellido) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setApellido'");
    }
    public void setTelefono(String telefono) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setTelefono'");
    }
    public void setCorreo(String correo) {
      
        throw new UnsupportedOperationException("Unimplemented method 'setCorreo'");
    }
    public void setFechaNacimiento(Object object) {
        
        throw new UnsupportedOperationException("Unimplemented method 'setFechaNacimiento'");
    }
    public void setSexo(String sexo) {

        throw new UnsupportedOperationException("Unimplemented method 'setSexo'");
    }
    public void setEstadoCivil(String estadoCivil) {
      
        throw new UnsupportedOperationException("Unimplemented method 'setEstadoCivil'");
    }
    public void setDireccion(String direccion) {
      
        throw new UnsupportedOperationException("Unimplemented method 'setDireccion'");
    }
    public void setNotas(String notas) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setNotas'");
    }
}