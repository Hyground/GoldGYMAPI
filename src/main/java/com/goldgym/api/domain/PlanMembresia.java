package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity @Table(name = "plan_membresia")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanMembresia {
  @Id @Column(columnDefinition = "uuid")
  private UUID id;

  @PrePersist public void pre(){ if(id==null) id=UUID.randomUUID(); }

  @Column(nullable=false) private String nombre;
  private String descripcion;
  @Column(nullable=false) private BigDecimal precio;
  @Column(name="duracion_dias", nullable=false) private Integer duracionDias;
  private Boolean activo = true;
}
