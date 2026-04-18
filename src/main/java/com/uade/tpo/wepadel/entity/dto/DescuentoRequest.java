package com.uade.tpo.wepadel.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DescuentoRequest {

    private Long productoId;
    private BigDecimal porcentaje;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}