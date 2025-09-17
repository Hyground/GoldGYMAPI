package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "persona",
        indexes = { @Index(name="uk_persona_correo", columnList = "correo", unique = true) })
public class Persona {

  public enum Sexo { M, F }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String nombre;

  @Column(length = 100, nullable = false)
  private String apellido;

  @Column(name = "fecha_nacimiento")
  private LocalDate fechaNacimiento;

  @Enumerated(EnumType.STRING)
  @Column(name = "sexo", length = 1, columnDefinition = "char(1)")
  private Sexo sexo; // CHECK ('M','F')

  @Column(name = "estado_civil", length = 30)
  private String estadoCivil;

  @Column(length = 30)
  private String telefono;

  @Column(length = 150, unique = true)
  private String correo;

  @Column(columnDefinition = "text")
  private String direccion;

  @Column(name = "telefono_emergencia", length = 30)
  private String telefonoEmergencia;

  @Column(columnDefinition = "text")
  private String notas;

  @Column(nullable = false)
  private Boolean activo = true;

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @Column(name = "actualizado_en", nullable = false)
  private LocalDateTime actualizadoEn;

  @PrePersist
  void prePersist() {
    this.creadoEn = this.actualizadoEn = LocalDateTime.now();
    if (activo == null) activo = true;
  }

  @PreUpdate
  void preUpdate() { this.actualizadoEn = LocalDateTime.now(); }
}
