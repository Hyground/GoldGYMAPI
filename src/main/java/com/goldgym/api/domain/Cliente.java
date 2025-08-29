package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name = "cliente")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Cliente {
  @Id @Column(columnDefinition = "uuid")
  private UUID id;

  @PrePersist public void pre(){ if(id==null) id=UUID.randomUUID(); }

  @OneToOne(optional = false) @JoinColumn(name = "persona_id")
  private Persona persona;

  @Column(unique = true) private String codigo;
  private String estado = "activo";
}
