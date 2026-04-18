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

    // Constructor para crear SistemaPuntos cuando usuario CLIENTE se registra
    // Se crea SOLO si el usuario está registrado Y es CLIENTE
    public SistemaPuntos(Usuario usuario, int conversion) {
        this.usuario = usuario;
        this.cantidad = 0;  // Inicia con 0 puntos acumulados
        this.conversion = conversion;
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

    @Column(name = "conversion", nullable = false)
    private int conversion;

}
