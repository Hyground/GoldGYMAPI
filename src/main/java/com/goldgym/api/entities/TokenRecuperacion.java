package com.goldgym.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "token_recuperacion")
public class TokenRecuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String canal;
    private String destino;
    private String codigo;
    private String token;

    @Column(name = "expira_en")
    private LocalDateTime expiraEn;

    @Column(name = "usado_en")
    private LocalDateTime usadoEn;

    private Integer intento;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;
}