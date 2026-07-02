package com.uade.tpo.wepadel.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.controllers.auth.AuthenticationRequest;
import com.uade.tpo.wepadel.controllers.auth.AuthenticationResponse;
import com.uade.tpo.wepadel.controllers.auth.ForgotPasswordRequest;
import com.uade.tpo.wepadel.controllers.auth.ForgotPasswordResponse;
import com.uade.tpo.wepadel.controllers.auth.MessageResponse;
import com.uade.tpo.wepadel.controllers.auth.RegisterRequest;
import com.uade.tpo.wepadel.controllers.auth.ResetPasswordRequest;
import com.uade.tpo.wepadel.controllers.config.JwtService;
import com.uade.tpo.wepadel.entity.PasswordResetToken;
import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.RolEnum;
import com.uade.tpo.wepadel.exceptions.BadCredentialsException;
import com.uade.tpo.wepadel.exceptions.InvalidResetTokenException;
import com.uade.tpo.wepadel.exceptions.UsuarioDuplicateException;
import com.uade.tpo.wepadel.repository.PasswordResetTokenRepository;
import com.uade.tpo.wepadel.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private static final String GENERIC_FORGOT_MESSAGE = "Si el email existe se enviarán instrucciones";

  private final UsuarioRepository usuarioRepository;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UsuarioService usuarioService;
  private final CarritoService carritoService;
  private final SistemaPuntosService sistemaPuntosService;

  public AuthenticationResponse register(RegisterRequest request) {
    if (usuarioRepository.findByMail(request.getEmail()).isPresent()) {
      throw new UsuarioDuplicateException("El email ya se encuentra registrado");
    }
    var user = Usuario.builder()
        .nombreApellido(request.getNombreApellido())
        .mail(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .rol(request.getRole())
        .build();

    usuarioRepository.save(user);

    // Si el usuario es CLIENTE, le creamos su Carrito y su Sistema de Puntos
    if (user.getRol() == RolEnum.CLIENTE) {
      carritoService.createCarrito(user.getId());
      sistemaPuntosService.createSistemaPuntos(user);
    }

    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .id(user.getId())
        .nombreApellido(user.getNombreApellido())
        .mail(user.getMail())
        .rol(user.getRol())
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
        .id(user.getId())
        .nombreApellido(user.getNombreApellido())
        .mail(user.getMail())
        .rol(user.getRol())
        .build();
  }

  @Transactional
  public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
    var userOptional = usuarioRepository.findByMail(request.getEmail().trim());

    if (userOptional.isEmpty()) {
      return ForgotPasswordResponse.builder()
          .message(GENERIC_FORGOT_MESSAGE)
          .build();
    }

    var user = userOptional.get();
    passwordResetTokenRepository.deleteByUsuario(user);

    var token = UUID.randomUUID().toString().replace("-", "");
    var resetToken = PasswordResetToken.builder()
        .token(token)
        .usuario(user)
        .expirationDate(LocalDateTime.now().plusMinutes(15))
        .used(false)
        .build();

    passwordResetTokenRepository.save(resetToken);

    return ForgotPasswordResponse.builder()
        .message("Se generó un código de recuperación")
        .token(token)
        .build();
  }

  @Transactional
  public MessageResponse resetPassword(ResetPasswordRequest request) {
    var resetToken = passwordResetTokenRepository.findByToken(request.getToken())
        .orElseThrow(() -> new InvalidResetTokenException("Token inválido o expirado"));

    if (resetToken.isUsed() || resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
      throw new InvalidResetTokenException("Token inválido o expirado");
    }

    var user = resetToken.getUsuario();
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    usuarioRepository.save(user);

    resetToken.setUsed(true);
    passwordResetTokenRepository.save(resetToken);

    return MessageResponse.builder()
        .message("Contraseña actualizada correctamente")
        .build();
  }
}
