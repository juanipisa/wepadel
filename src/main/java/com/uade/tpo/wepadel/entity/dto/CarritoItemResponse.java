package com.uade.tpo.wepadel.entity.dto;

import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItemResponse {
    private Long id;
    private Long productoId;
    private int cantidad;
    private BigDecimal precioConDescuento;
}
