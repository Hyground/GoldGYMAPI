package com.goldgym.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "parametro_sistema")
public class ParametroSistema {

    @Id
    private String clave;

    private String valor;
    private String descripcion;
}