package com.uade.tpo.wepadel.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarritoItemRequest {
    @NotNull
    private Long productoId;
    @NotNull
    private int cantidad;
}
