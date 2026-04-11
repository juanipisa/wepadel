package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.RolEnum;
import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.UsuarioRequest;
import com.uade.tpo.wepadel.exceptions.UsuarioDuplicateException;
import com.uade.tpo.wepadel.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId);
    }

    public Usuario createUsuario(UsuarioRequest request) throws UsuarioDuplicateException {
        // Validar que email no exista si se proporciona
        if (usuarioRepository.findByMail(request.getMail()).isPresent()) {
            throw new UsuarioDuplicateException();
        }
        // Si no tiene rol, es CLIENTE por default
        RolEnum rol = request.getRol() != null ? request.getRol() : RolEnum.CLIENTE;
        return usuarioRepository.save(new Usuario(request.getNombreApellido(), request.getMail(),
                request.getPassword(), rol));
    }

    public Optional<Usuario> updateUsuario(Long usuarioId, UsuarioRequest request) {
        return usuarioRepository.findById(usuarioId).map(usuario -> {
            if (request.getNombreApellido() != null) usuario.setNombreApellido(request.getNombreApellido());
            if (request.getMail() != null) usuario.setMail(request.getMail());
            if (request.getPassword() != null) usuario.setPassword(request.getPassword());
            if (request.getRol() != null) usuario.setRol(request.getRol());
            return usuarioRepository.save(usuario);
        });
    }

}
