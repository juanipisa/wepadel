package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Usuario;
import com.uade.tpo.marketplace.entity.dto.UsuarioRequest;
import com.uade.tpo.marketplace.exceptions.UsuarioDuplicateException;

public interface UsuarioService {
    public List<Usuario> getUsuarios();
    public Optional<Usuario> getUsuarioById(Long usuarioId);
    public Usuario createUsuario(UsuarioRequest request) throws UsuarioDuplicateException;
    public Optional<Usuario> updateUsuario(Long usuarioId, UsuarioRequest request);
}
