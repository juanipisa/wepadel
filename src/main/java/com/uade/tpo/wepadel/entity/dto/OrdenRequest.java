package com.uade.tpo.wepadel.entity.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrdenRequest {
    private Long usuarioId;
    private String direccion;
    private String cp;
    private BigDecimal montoEnvio;
    private Boolean usaPuntos;
}
