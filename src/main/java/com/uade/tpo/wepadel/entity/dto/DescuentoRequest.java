package com.uade.tpo.wepadel.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DescuentoRequest {
    @NotNull
    private Long productoId;
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @DecimalMax(value = "100", inclusive = false)
    private BigDecimal porcentaje;
    @NotNull
    private LocalDateTime fechaInicio;
    @NotNull
    private LocalDateTime fechaFin;
}