package com.uade.tpo.marketplace.entity.dto;

import java.math.BigDecimal;

import com.uade.tpo.marketplace.entity.CategoriaEnum;

import lombok.Data;

@Data
public class ProductoRequest {
    private String descripcion;
    private BigDecimal precio;
    private CategoriaEnum categoria;
    private Boolean estaHabilitado;
}
