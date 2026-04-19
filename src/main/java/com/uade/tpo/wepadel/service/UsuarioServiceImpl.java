package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.UsuarioRequest;
import com.uade.tpo.wepadel.exceptions.InvalidUserDataException;
import com.uade.tpo.wepadel.exceptions.UsuarioDuplicateException;
import com.uade.tpo.wepadel.exceptions.UsuarioNotFoundException;
import com.uade.tpo.wepadel.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{12,}$";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());
    }

    public Usuario updateUsuario(Long usuarioId, UsuarioRequest request) {
        validarActualizacion(usuarioId, request);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());
        if (request.getNombreApellido() != null) {
            usuario.setNombreApellido(request.getNombreApellido());
        }
        if (request.getMail() != null) {
            usuario.setMail(request.getMail());
        }
        if (request.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRol() != null) {
            usuario.setRol(request.getRol());
        }
        return usuarioRepository.save(usuario);
    }

    public void validarUsuario(String email, String password) {
        if (email == null || password == null) {
            throw new InvalidUserDataException("Email y contraseña son obligatorios");
        }
        validarMailDuplicado(email, Optional.empty());
        validarFormatoMail(email);
        validarFormatoPassword(password);
    }

    private void validarActualizacion(Long usuarioId, UsuarioRequest request) {
        String mail = request.getMail();
        String password = request.getPassword();
        if (mail != null) {
            validarMailDuplicado(mail, Optional.of(usuarioId));
            validarFormatoMail(mail);
        }
        if (password != null) {
            validarFormatoPassword(password);
        }
    }

    private void validarMailDuplicado(String email, Optional<Long> excluirUsuarioId) {
        if (excluirUsuarioId.isEmpty()) {
            if (usuarioRepository.findByMail(email).isPresent()) {
                throw new UsuarioDuplicateException("El email '" + email + "' ya se encuentra registrado");
            }
            return;
        }
        Long id = excluirUsuarioId.get();
        if (usuarioRepository.findByMail(email)
                .filter(u -> !u.getId().equals(id))
                .isPresent()) {
            throw new UsuarioDuplicateException("El email ya se encuentra registrado");
        }
    }

    private void validarFormatoMail(String email) {
        if (!email.matches(REGEX_EMAIL)) {
            throw new InvalidUserDataException("El formato del mail no es válido");
        }
    }

    private void validarFormatoPassword(String password) {
        if (!password.matches(REGEX_PASSWORD)) {
            throw new InvalidUserDataException(
                    "La contraseña debe tener al menos 12 caracteres, incluir una mayúscula, un número y un símbolo");
        }
    }
}
