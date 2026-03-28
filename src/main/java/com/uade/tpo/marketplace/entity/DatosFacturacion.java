package com.uade.tpo.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class DatosFacturacion {

    public DatosFacturacion() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "compra_id", nullable = false)
    private Long compraId;

    @Column(name = "nro_tarjeta", nullable = false)
    private String nroTarjeta;

    @Column(name = "vencimiento", nullable = false)
    private String vencimiento;

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "cvv", nullable = false)
    private String cvv;

    @Column(name = "nombre_titular", nullable = false)
    private String nombreTitular;

    @Column(name = "cuotas", nullable = false)
    private int cuotas;

}
