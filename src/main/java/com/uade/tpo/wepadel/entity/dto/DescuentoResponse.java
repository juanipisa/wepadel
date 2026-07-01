package com.uade.tpo.wepadel.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoResponse {

    private Long id;
    private BigDecimal porcentaje;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean activo;

}
