package com.uade.tpo.wepadel.entity.dto;

import com.uade.tpo.wepadel.entity.RolEnum;
import lombok.Data;

@Data
public class UsuarioRequest {
    private String nombreApellido;
    private String mail;
    private String password;
    private RolEnum rol;
}
