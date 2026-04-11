package com.uade.tpo.wepadel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SistemaPuntos {

    // Constructor vacío requerido por JPA
    public SistemaPuntos() {
    }

    // Constructor para crear SistemaPuntos cuando usuario CLIENTE se registra
    // Se crea SOLO si el usuario está registrado Y es CLIENTE
    public SistemaPuntos(Long usuarioId, int conversion) {
        this.usuarioId = usuarioId;
        this.cantidad = 0;  // Inicia con 0 puntos acumulados
        this.conversion = conversion;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private Long usuarioId;

    @Column(name = "cantidad", nullable = false)
    private int cantidad = 0;

    @Column(name = "conversion", nullable = false)
    private int conversion; // TODO: hacer la lógica de conversión

}
