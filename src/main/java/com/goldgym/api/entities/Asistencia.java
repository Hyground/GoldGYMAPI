package com.goldgym.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(
    name = "asistencia",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cliente_id", "fecha"})
    }
)
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    private LocalDate fecha;
    private Boolean presente;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;
}