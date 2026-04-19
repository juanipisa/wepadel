package com.uade.tpo.wepadel.controllers.auth;

import com.uade.tpo.wepadel.entity.RolEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @NotBlank
  private String nombreApellido;

  @NotNull
  @Email
  private String email;

  @NotNull
  @Size(min = 12)
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{12,}$",
      message = "La contraseña debe tener 12+ caracteres, una mayúscula, un número y un símbolo"
  )
  private String password;

  @NotNull
  private RolEnum role;
}