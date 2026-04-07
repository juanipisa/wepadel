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
public class Usuario {

    public Usuario() {
        // Constructor para usuario INVITADO (sin datos personales, rol CLIENTE por default)
        this.nombreApellido = null;
        this.mail = null;
        this.password = null;
        this.estaRegistrado = false;
        this.rol = RolEnum.CLIENTE;
        this.fecha_creacion = null;
    }

    // Constructor para usuario REGISTRADO (cliente o administrador)
    public Usuario(String nombreApellido, String mail, String password, RolEnum rol) {
        this.nombreApellido = nombreApellido;
        this.mail = mail;
        this.password = password;
        this.estaRegistrado = true;
        this.rol = rol;
        this.fecha_creacion = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_apellido", nullable = false)
    private String nombreApellido;

    @Column(name = "mail", nullable = true, unique = false)
    private String mail;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "estaRegistrado", nullable = false)
    private Boolean estaRegistrado = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolEnum rol;

    @Column(name = "fecha_creacion")
    private LocalDateTime fecha_creacion;

}
