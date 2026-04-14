package com.uade.tpo.wepadel.entity;

import java.time.LocalDateTime;

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
public class Stock {

    public Stock() {
    }

    public Stock(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.ultimaModificacion = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "ultima_modificacion", nullable = false)
    private LocalDateTime ultimaModificacion;

}
