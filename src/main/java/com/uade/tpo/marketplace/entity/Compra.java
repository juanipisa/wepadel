package com.uade.tpo.marketplace.entity;

import java.time.LocalDateTime;

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
public class Compra {

    public Compra() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "cp", nullable = false)
    private String cp;

    @Column(name = "monto_envio", nullable = false)
    private Double montoEnvio;

    @Column(name = "monto_final", nullable = false)
    private Double montoFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoOrdenEnum estado;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

}
