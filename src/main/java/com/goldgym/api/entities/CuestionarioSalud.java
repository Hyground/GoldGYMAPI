package com.goldgym.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "cuestionario_salud")
public class CuestionarioSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    private Boolean lesionOsea;
    private String descripcionLesionOsea;

    private Boolean lesionMuscular;
    private String descripcionLesionMuscular;

    private Boolean enfermedadCardiovascular;
    private Boolean seAsfixia;
    private Boolean embarazada;
    private Boolean anemia;
    private Boolean practicaDeportes;
    private Boolean asma;
    private Boolean epilepsia;
    private Boolean diabetes;
    private Boolean mareos;
    private Boolean desmayos;
    private Boolean nausea;
    private Boolean dificultadRespirar;

    private Boolean previoGimnasio;
    private String objetivo;

    /** Guardamos el JSON como String para simplificar */
    private String extra;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}