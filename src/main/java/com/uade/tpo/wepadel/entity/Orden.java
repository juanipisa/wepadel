package com.uade.tpo.wepadel.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Orden {

    public Orden() {
    }

    // Constructor para crear una orden cuando el usuario hace clic en pagar
    public Orden(Usuario usuario, String direccion, String cp, BigDecimal montoEnvio,
                 BigDecimal subtotal, BigDecimal total, Boolean usaPuntos, int puntosGenerados) {
        this.usuario = usuario;
        this.direccion = direccion;
        this.cp = cp;
        this.montoEnvio = montoEnvio;
        this.subtotal = subtotal;
        this.total = total;
        this.estado = EstadoOrdenEnum.CONFIRMADA;
        this.fechaCompra = LocalDateTime.now();
        this.usaPuntos = usaPuntos;
        this.puntosGenerados = puntosGenerados;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "cp", nullable = false)
    private String cp;

    @Column(name = "monto_envio", nullable = false)
    private BigDecimal montoEnvio;

    // Subtotal: suma de todos los productos en el carrito
    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    // Total final: montoEnvio + subtotal - (puntos usados convertidos a descuento)
    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenItem> items = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoOrdenEnum estado;

    // Fecha de creación de la orden (cuando se hace clic en pagar)
    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    // Indica si el usuario utilizó puntos para esta compra
    // false si es usuario invitado
    @Column(name = "usa_puntos", nullable = false)
    private Boolean usaPuntos = false;

    // Puntos generados por esta compra
    // Se suman al usuario cuando la orden se confirma
    @Column(name = "puntos_generados", nullable = false)
    private int puntosGenerados = 0;

    // Email para gestionar reembolsos
    // Se envía formulario por email, no se guarda en BD
    @Column(name = "mail_reembolsos")
    private String mailReembolsos;

}
