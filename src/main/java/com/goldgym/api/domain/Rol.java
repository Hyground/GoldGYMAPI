package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "rol")
public class Rol {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50, nullable = false, unique = true)
  private String nombre; // 'ADMIN', 'RECEPCIONISTA', etc.
}
