package com.uade.tpo.wepadel.entity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockRequest {

    @NotNull
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Max(value = Integer.MAX_VALUE, message = "La cantidad de stock supera el máximo permitido")
    private Integer cantidad;
}
