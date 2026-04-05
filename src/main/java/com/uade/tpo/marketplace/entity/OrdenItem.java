package com.uade.tpo.marketplace.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class OrdenItem {

    public OrdenItem() {
    }

    // Se crea automáticamente cuando se confirma la orden
    public OrdenItem(Long ordenId, Long productoId, int cantidad, BigDecimal precioUnitario) {
        this.ordenId = ordenId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden_id", nullable = false)
    private Long ordenId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    // Se guarda para historial permanente, aunque el precio del producto después cambie
    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

}
