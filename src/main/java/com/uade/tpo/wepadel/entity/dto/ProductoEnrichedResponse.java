package com.uade.tpo.wepadel.entity.dto;

import java.math.BigDecimal;
import java.util.List;

import com.uade.tpo.wepadel.entity.CategoriaEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEnrichedResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private CategoriaEnum categoria;
    private Boolean estaHabilitado;
    private int stock;
    private ImagenCatalogoResponse imagenPrincipal;
    private List<DescuentoResponse> descuentos;

}
