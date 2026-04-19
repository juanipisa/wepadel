package com.uade.tpo.wepadel.controllers.auth;

import com.uade.tpo.wepadel.entity.RolEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String nombreApellido;
  private String email;
  private String password;
  private RolEnum role;
}