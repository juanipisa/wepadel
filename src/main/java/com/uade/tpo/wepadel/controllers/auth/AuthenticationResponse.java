package com.uade.tpo.wepadel.controllers.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uade.tpo.wepadel.entity.RolEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;

  private Long id;

  private String nombreApellido;

  private String mail;

  private RolEnum rol;
}