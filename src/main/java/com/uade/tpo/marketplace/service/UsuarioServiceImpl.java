package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Usuario;
import com.uade.tpo.marketplace.entity.dto.UsuarioRequest;
import com.uade.tpo.marketplace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.marketplace.repository.UsuarioRepository;

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
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.stream().anyMatch(u -> u.getMail().equals(request.getMail()))) {
            throw new UsuarioDuplicateException();
        }
        return usuarioRepository.save(new Usuario(request.getNombreApellido(), request.getMail(), request.getPassword()));
    }

    public Optional<Usuario> updateUsuario(Long usuarioId, UsuarioRequest request) {
        return usuarioRepository.findById(usuarioId).map(usuario -> {
            if (request.getNombreApellido() != null) usuario.setNombreApellido(request.getNombreApellido());
            if (request.getMail() != null) usuario.setMail(request.getMail());
            if (request.getPassword() != null) usuario.setPassword(request.getPassword());
            return usuarioRepository.save(usuario);
        });
    }

}
