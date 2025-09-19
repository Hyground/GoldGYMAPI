package com.goldgym.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "evento_login")
public class EventoLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private Boolean exito;
    private String ip;

    @Column(name = "user_agent")
    private String userAgent;

    private String detalle;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;
}