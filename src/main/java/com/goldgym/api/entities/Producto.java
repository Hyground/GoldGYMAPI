package com.goldgym.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String categoria;

    @Column(name = "tipo_medida")
    private String tipoMedida; // UNIDAD o SCOOP

    @Column(name = "scoops_por_envase")
    private Integer scoopsPorEnvase;

    @Column(name = "precio_venta")
    private Double precioVenta;

    @Column(name = "stock_cantidad")
    private Double stockCantidad;

    @Column(name = "stock_minimo_alerta")
    private Double stockMinimoAlerta;

    private Boolean activo;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}