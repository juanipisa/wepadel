package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.UsuarioRequest;
import com.uade.tpo.wepadel.exceptions.UsuarioDuplicateException;

public interface UsuarioService {
    public List<Usuario> getUsuarios();
    public Optional<Usuario> getUsuarioById(Long usuarioId);
    public Usuario createUsuario(UsuarioRequest request) throws UsuarioDuplicateException;
    public Optional<Usuario> updateUsuario(Long usuarioId, UsuarioRequest request);
}
