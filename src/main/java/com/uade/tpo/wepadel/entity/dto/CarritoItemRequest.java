package com.uade.tpo.wepadel.entity.dto;

import lombok.Data;

@Data
public class CarritoItemRequest {
    private Long productoId;
    private int cantidad;
}
