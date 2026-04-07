package com.uade.tpo.marketplace.entity;

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
public class CarritoItem {

    public CarritoItem() {
    }

    public CarritoItem(Long carritoId, Long productoId, int cantidad) {
        this.carritoId = carritoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carrito_id", nullable = false)
    private Long carritoId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

}
