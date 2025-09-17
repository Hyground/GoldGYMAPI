package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "usuario_rol")
public class UsuarioRol {

  @EmbeddedId
  private UsuarioRolId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("usuarioId")
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("rolId")
  @JoinColumn(name = "rol_id", nullable = false)
  private Rol rol;

  // ---- ID compuesto ----
  @Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
  @Embeddable
  public static class UsuarioRolId implements Serializable {
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "rol_id")
    private Long rolId;
  }
}
