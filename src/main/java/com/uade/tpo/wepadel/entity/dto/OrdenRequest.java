package com.uade.tpo.wepadel.entity.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrdenRequest {
    private Long usuario;
    private String direccion;
    private String cp;
    private BigDecimal montoEnvio;
    private Boolean usaPuntos;
    /** Cantidad de puntos a canjear en la compra (si usaPuntos es true). */
    private Integer puntosUsados;
}
