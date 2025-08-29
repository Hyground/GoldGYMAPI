package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "persona")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Persona {
  @Id @Column(columnDefinition = "uuid")
  private UUID id;

  @PrePersist public void pre(){ if(id==null) id=UUID.randomUUID(); }

  @Column(nullable=false) private String nombres;
  @Column(nullable=false) private String apellidos;
  private LocalDate fechaNacimiento;
  private String sexo;
  private String estadoCivil;
  private String telefono;

  @Column(unique = true) private String correo;

  private String direccion;
  private String telefonoEmergencia;
  private Boolean activo = true;
}
