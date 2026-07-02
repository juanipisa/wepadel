package com.uade.tpo.wepadel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.wepadel.entity.PasswordResetToken;
import com.uade.tpo.wepadel.entity.Usuario;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
  Optional<PasswordResetToken> findByToken(String token);

  void deleteByUsuario(Usuario usuario);
}
