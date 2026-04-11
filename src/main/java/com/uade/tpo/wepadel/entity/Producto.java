package com.uade.tpo.wepadel.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Producto {

    public Producto() {
    }

    public Producto(String descripcion, BigDecimal precio, CategoriaEnum categoria) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.estaHabilitado = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaEnum categoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    @Column(name = "estaHabilitado", nullable = false)
    private Boolean estaHabilitado = true;

}
