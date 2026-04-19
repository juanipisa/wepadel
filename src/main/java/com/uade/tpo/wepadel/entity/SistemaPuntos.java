package com.uade.tpo.wepadel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class SistemaPuntos {

    // Constructor vacío requerido por JPA
    public SistemaPuntos() {
    }

    public SistemaPuntos(Usuario usuario) {
        this.usuario = usuario;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "cantidad", nullable = false)
    private int cantidad = 0;

    /** Pesos por cada punto al canjear (ej. 1 punto = $5). Valor inicial al crear la fila. */
    @Column(name = "conversion", nullable = false)
    private int conversion = 5;

}
