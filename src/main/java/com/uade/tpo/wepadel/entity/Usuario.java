package com.uade.tpo.wepadel.entity;

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

    // Constructor requerido por JPA — no usar directamente
    protected Usuario() {
    }

    public Usuario(String nombreApellido, String mail, String password, RolEnum rol) {
        this.nombreApellido = nombreApellido;
        this.mail = mail;
        this.password = password;
        this.rol = rol;
        this.fechaCreacion = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_apellido", nullable = false)
    private String nombreApellido;

    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolEnum rol;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

}
