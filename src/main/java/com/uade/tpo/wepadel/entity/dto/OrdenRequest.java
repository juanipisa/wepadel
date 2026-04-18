package com.uade.tpo.wepadel.entity.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class OrdenRequest {

    @NotNull
    private Long usuario;

    @NotBlank
    private String direccion;

    @NotBlank
    private String cp;

    @NotNull
    @DecimalMin(value = "0", inclusive = true)
    private BigDecimal montoEnvio;

    private Boolean usaPuntos;

    /** Cantidad de puntos a canjear en la compra (si usaPuntos es true). */
    private Integer puntosUsados;
}
