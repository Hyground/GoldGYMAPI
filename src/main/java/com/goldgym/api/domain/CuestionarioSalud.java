package com.goldgym.api.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "cuestionario_salud")
public class CuestionarioSalud {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @Column(name = "fecha_registro", nullable = false)
  private LocalDate fechaRegistro = LocalDate.now();

  private Boolean lesionOsea;
  @Column(name = "descripcion_lesion_osea", columnDefinition = "text")
  private String descripcionLesionOsea;

  private Boolean lesionMuscular;
  @Column(name = "descripcion_lesion_muscular", columnDefinition = "text")
  private String descripcionLesionMuscular;

  private Boolean enfermedadCardiovascular;
  private Boolean seAsfixia;
  private Boolean embarazada;
  private Boolean anemia;
  private Boolean practicaDeportes;
  private Boolean asma;
  private Boolean epilepsia;
  private Boolean diabetes;
  private Boolean mareos;
  private Boolean desmayos;
  private Boolean nausea;
  private Boolean dificultadRespirar;
  private Boolean previoGimnasio;

  @Column(columnDefinition = "text")
  private String objetivo;

  @Column(columnDefinition = "jsonb")
  private String extra; // JSON crudo como String (simplificado)

  @Column(name = "creado_en", nullable = false)
  private LocalDateTime creadoEn;

  @Column(name = "actualizado_en", nullable = false)
  private LocalDateTime actualizadoEn;

  @PrePersist
  void prePersist() {
    this.creadoEn = this.actualizadoEn = LocalDateTime.now();
  }

  @PreUpdate
  void preUpdate() { this.actualizadoEn = LocalDateTime.now(); }
}
