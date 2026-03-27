package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data
public class CompraRequest {
    private Long usuarioId;
    private Long carritoId;
    private String direccion;
    private String cp;
    private Double montoEnvio;
    private String nroTarjeta;
    private String vencimiento;
    private String dni;
    private String cvv;
    private String nombreTitular;
    private int cuotas;
}
