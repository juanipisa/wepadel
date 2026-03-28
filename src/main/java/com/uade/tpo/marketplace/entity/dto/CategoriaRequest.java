package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data
public class CategoriaRequest {
    private Long categoriaId;
    private String descripcion;
    private Boolean habilitada;
}
