package com.uade.tpo.wepadel.entity.dto;

import java.math.BigDecimal;

import com.uade.tpo.wepadel.entity.CategoriaEnum;

import lombok.Data;

@Data
public class ProductoRequest {
    private String descripcion;
    private BigDecimal precio;
    private CategoriaEnum categoria;
    private Boolean estaHabilitado;
}
