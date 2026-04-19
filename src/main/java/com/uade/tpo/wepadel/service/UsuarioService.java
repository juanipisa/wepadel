package com.uade.tpo.wepadel.service;

import java.util.List;

import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.UsuarioRequest;

public interface UsuarioService {
    public List<Usuario> getUsuarios();
    public Usuario getUsuarioById(Long usuarioId);
    public Usuario updateUsuario(Long usuarioId, UsuarioRequest request);
    public void validarUsuario(String email, String password);
}
