package com.goldgym.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String sexo;

    @Column(name = "estado_civil")
    private String estadoCivil;

    private String telefono;
    private String correo;
    private String direccion;

    @Column(name = "telefono_emergencia")
    private String telefonoEmergencia;

    private String notas;
    private Boolean activo;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
