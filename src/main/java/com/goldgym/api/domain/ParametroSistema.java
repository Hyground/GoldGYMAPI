package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "parametro_sistema")
public class ParametroSistema {

  @Id
  @Column(length = 60)
  private String clave;

  @Column(length = 120, nullable = false)
  private String valor;

  @Column(columnDefinition = "text")
  private String descripcion;
}
