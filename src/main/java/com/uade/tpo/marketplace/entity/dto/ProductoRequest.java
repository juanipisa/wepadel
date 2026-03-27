package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data
public class ProductoRequest {
    private Long productoId;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Long categoriaId;
    private Boolean habilitado;
}
