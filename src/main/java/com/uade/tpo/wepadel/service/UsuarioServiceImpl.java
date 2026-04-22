package com.uade.tpo.wepadel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.RolEnum;
import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.UsuarioRequest;
import com.uade.tpo.wepadel.exceptions.AccesoDenegadoException;
import com.uade.tpo.wepadel.exceptions.UsuarioDuplicateException;
import com.uade.tpo.wepadel.exceptions.UsuarioNotFoundException;
import com.uade.tpo.wepadel.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

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
        String mailActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario activo = usuarioRepository.findByMail(mailActual)
                .orElseThrow(UsuarioNotFoundException::new);

        if (activo.getRol() == RolEnum.CLIENTE && !activo.getId().equals(usuarioId)) {
            throw new AccesoDenegadoException();
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(UsuarioNotFoundException::new);

        if (request.getNombreApellido() != null) {
            usuario.setNombreApellido(request.getNombreApellido());
        }
        if (request.getMail() != null && activo.getRol() != RolEnum.ADMINISTRADOR) {
            validarMailDuplicado(request.getMail(), usuarioId);
            usuario.setMail(request.getMail());
        }
        if (request.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    private void validarMailDuplicado(String email, Long excluirUsuarioId) {
        if (usuarioRepository.findByMail(email)
                .filter(u -> !u.getId().equals(excluirUsuarioId))
                .isPresent()) {
            throw new UsuarioDuplicateException("El email ya se encuentra registrado");
        }
    }
}
