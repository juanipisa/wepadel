package com.uade.tpo.wepadel.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.controllers.auth.AuthenticationRequest;
import com.uade.tpo.wepadel.controllers.auth.AuthenticationResponse;
import com.uade.tpo.wepadel.controllers.auth.RegisterRequest;
import com.uade.tpo.wepadel.controllers.config.JwtService;
import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.exceptions.BadCredentialsException;
import com.uade.tpo.wepadel.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UsuarioService usuarioService;

  public AuthenticationResponse register(RegisterRequest request) {
    // Validar formato y duplicados usando el service de Usuario
    usuarioService.validarUsuario(request.getEmail(), request.getPassword());

    var user = Usuario.builder()
        .nombreApellido(request.getNombreApellido())
        .mail(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .rol(request.getRole())
        .build();

    usuarioRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()));

    var user = usuarioRepository.findByMail(request.getEmail())
        .orElseThrow(() -> new BadCredentialsException("Email o contraseña incorrectos"));
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .build();
  }
}
