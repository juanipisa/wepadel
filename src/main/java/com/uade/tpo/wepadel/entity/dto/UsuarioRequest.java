package com.uade.tpo.wepadel.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequest {

    @Size(min = 2, message = "El nombre y apellido debe tener al menos 2 caracteres")
    private String nombreApellido;

    @Email(message = "El formato del mail no es válido")
    private String mail;

    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{12,}$",
        message = "La contraseña debe tener 12+ caracteres, una mayúscula, un número y un símbolo"
    )
    private String password;
}
