package com.uade.tpo.wepadel.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class OrdenItem {

    public OrdenItem() {
    }

    // Se crea automáticamente cuando se confirma la orden
    public OrdenItem(Orden orden, Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.orden = orden;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    @JsonIgnore
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    // Se guarda para historial permanente, aunque el precio del producto después cambie
    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

}
