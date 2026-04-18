package com.uade.tpo.wepadel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "imagen")
public class Imagen {

    public Imagen(Producto producto, byte[] contenido, String nombre) {
        this.producto = producto;
        this.contenido = contenido;
        this.nombre = nombre;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Lob
    @Column(name = "contenido", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] contenido;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

}
