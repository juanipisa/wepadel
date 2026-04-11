package com.uade.tpo.wepadel.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Stock {

    public Stock() {
    }

    public Stock(Long productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.ultima_modificacion = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false, unique = true)
    private Long productoId;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "ultima_modificacion", nullable = false)
    private LocalDateTime ultima_modificacion;

}
