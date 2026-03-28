package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String nombreApellido;
    private String mail;
    private String password;
}
