package com.goldgym.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "medicion")
public class Medicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_medicion")
    private LocalDate fechaMedicion;

    @Column(name = "peso_kg")
    private Double pesoKg;

    @Column(name = "altura_cm")
    private Double alturaCm;

    @Column(name = "brazo_cm")
    private Double brazoCm;

    @Column(name = "pierna_cm")
    private Double piernaCm;

    @Column(name = "cintura_cm")
    private Double cinturaCm;

    @Column(name = "cuello_cm")
    private Double cuelloCm;

    private String notas;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}