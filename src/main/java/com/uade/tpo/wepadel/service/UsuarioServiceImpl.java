package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.wepadel.exceptions.UsuarioDatosInvalidosException;
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
        validarUsuario(request);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());

        if (request.getNombreApellido() != null) usuario.setNombreApellido(request.getNombreApellido());
        if (request.getMail() != null) usuario.setMail(request.getMail());
        if (request.getPassword() != null) usuario.setPassword(request.getPassword());
        if (request.getRol() != null) usuario.setRol(request.getRol());

        return usuarioRepository.save(usuario);
    }

    private void validarUsuario(UsuarioRequest request) {
        // 1. Validar Mail (formato nombre@algo.com)
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (request.getMail() == null || !request.getMail().matches(regexEmail)) {
            throw new UsuarioDatosInvalidosException("El formato del mail no es válido");
        }

        // 2. Validar Contraseña (Min 12 caracteres, Mayúscula, Número y Símbolo)
        // Explicación: (?=.*[0-9]) busca número, (?=.*[a-z]) minúscula, (?=.*[A-Z]) mayúscula, (?=.*[@#$%^&+=!]) símbolo
        String regexPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{12,}$";
        if (request.getPassword() == null || !request.getPassword().matches(regexPassword)) {
            throw new UsuarioDatosInvalidosException(
                    "La contraseña debe tener 12+ caracteres, una mayúscula, un número y un símbolo");
        }
    }

}
