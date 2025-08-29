package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "membresia")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Membresia {
  @Id @Column(columnDefinition = "uuid")
  private UUID id;

  @PrePersist public void pre(){ if(id==null) id=UUID.randomUUID(); }

  @ManyToOne(optional=false) @JoinColumn(name="cliente_id")
  private Cliente cliente;

  @ManyToOne(optional=false) @JoinColumn(name="plan_id")
  private PlanMembresia plan;

  private LocalDate fechaInicio;
  private LocalDate fechaFin;

  @Column(nullable=false) private BigDecimal precioContratado;
  private String estado = "activa";
}
