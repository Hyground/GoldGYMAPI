package com.goldgym.api.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioDTO {
  private Long id;
  private String username;
  private String correo;   // tomado de Persona
  private String nombre;   // tomado de Persona
  private String apellido; // tomado de Persona
}
