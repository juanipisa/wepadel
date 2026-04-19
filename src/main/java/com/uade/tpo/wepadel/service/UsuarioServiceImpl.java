package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.wepadel.exceptions.InvalidUserDataException;
import com.uade.tpo.wepadel.exceptions.UsuarioDuplicateException;
import com.uade.tpo.wepadel.exceptions.UsuarioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.UsuarioRequest;
import com.uade.tpo.wepadel.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());
    }

    public Usuario updateUsuario(Long usuarioId, UsuarioRequest request) {
        validarUsuario(request.getMail(), request.getPassword());

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());

        if (request.getNombreApellido() != null) usuario.setNombreApellido(request.getNombreApellido());
        if (request.getMail() != null) usuario.setMail(request.getMail());
        if (request.getPassword() != null) usuario.setPassword(request.getPassword());
        if (request.getRol() != null) usuario.setRol(request.getRol());

        return usuarioRepository.save(usuario);
    }

    public void validarUsuario(String email, String password) {
        // 1. Validar si ya existe un usuario con ese email (Duplicado)
        if (usuarioRepository.findByMail(email).isPresent()) {
            throw new UsuarioDuplicateException("El email '" + email + "' ya se encuentra registrado");
        }

        // 2. Validar Formato Mail (nombre@algo.com)
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || !email.matches(regexEmail)) {
            throw new InvalidUserDataException("El formato del mail no es válido");
        }

        // 3. Validar Contraseña (Min 12 caracteres, Mayúscula, Número y Símbolo)
        String regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{12,}$";
        if (password == null || !password.matches(regexPassword)) {
            throw new InvalidUserDataException(
                    "La contraseña debe tener al menos 12 caracteres, incluir una mayúscula, un número y un símbolo");
        }
    }

}
